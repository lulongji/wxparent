package com.yuntongxun.controller;

import com.yuntongxun.base.bean.Result;
import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.base.wechat.util.HttpClientUtils;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.constants.WxRedisConstant;
import com.yuntongxun.model.po.open.OpenConfig;
import com.yuntongxun.model.po.open.WxPlatformInfo;
import com.yuntongxun.model.properties.WxOpenProperties;
import com.yuntongxun.service.open.OpenService;
import com.yuntongxun.service.open.WxPlatformInfoService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: 微信开放平台接收消息
 * @Author: lulongji
 * @Date: Created in 15:23 2018/9/29
 */

@Controller
@RequestMapping("/open/api")
public class OpenApiController {

    private static final Logger logger = LoggerFactory.getLogger(OpenApiController.class);


    @Autowired
    private WxOpenProperties wxOpenProperties;

    @Autowired
    private OpenService openService;

    @Autowired
    private WxPlatformInfoService wxPlatformInfoService;


    @GetMapping("/{accessId}/success")
    public String success(Map<String, Object> map, @PathVariable("accessId") String accessId) {
        map.put("accessId", accessId);
        return "/success";
    }

    /**
     * 授权入口
     *
     * @param accessId
     * @return
     */
    @GetMapping("/auth")
    public String gotouth(Map<String, Object> map, @RequestParam("accessId") String accessId) {
        map.put("authUri", wxOpenProperties.getUri() + "/open/api/auth/goto_auth_url?accessId=" + accessId + "&redurl=" + wxOpenProperties.getUri() + "/open/api/" + accessId + "/success");
        return "/auth";
    }

    /**
     * 授权地址
     *
     * @param response
     * @param accessId
     * @param redurl
     * @param authType
     * @param bizAppid
     */
    @GetMapping("/auth/goto_auth_url")
    public void gotoPreAuthUrl(HttpServletResponse response,
                               @RequestParam("accessId") String accessId,
                               @RequestParam("redurl") String redurl,
                               @RequestParam(value = "authType", required = false) String authType,
                               @RequestParam(value = "bizAppid", required = false) String bizAppid) {
        String hrefurl;
        String redirecturi;
        StringBuilder preAuthUrl = new StringBuilder();
        try {
            String preAuthCode = openService.getPreAuthCodeCache();
            if (StringUtils.isBlank(preAuthCode)) {
                throw new Exception("accessId:" + accessId + "==>gotoPreAuthUrl#exception:preAuthCode is null.");
            }
            String uri = wxOpenProperties.getUri() + "/open/api/[ACCESSID]/authcallback".replace("[ACCESSID]", accessId);
            redirecturi = java.net.URLEncoder.encode(uri, "utf-8");
            redurl = java.net.URLDecoder.decode(redurl, "utf-8");
            RedisTemplateUtil.set(WxRedisConstant.OPEN_ACCESS_REDURL + accessId, redurl, 0, TimeUnit.SECONDS);

            hrefurl = WxConstant.AUTH_ENTRY_URL.replace("[COMPONENT_APPID]", wxOpenProperties.getComponentAppId())
                    .replace("[PRE_AUTH_CODE]", preAuthCode).replace("[REDIRECT_URI]", redirecturi);
            preAuthUrl.append(hrefurl);
            if (StringUtils.isNotEmpty(authType)) {
                preAuthUrl.append("&auth_type=").append(authType);
            }

            if (StringUtils.isNotEmpty(bizAppid)) {
                preAuthUrl.append("&biz_appid=").append(bizAppid);
            }
            hrefurl = preAuthUrl.toString();
            response.sendRedirect(hrefurl);
        } catch (Exception e) {
            logger.error("accessId:" + accessId + "==>gotoPreAuthUrl#exception:", e);
            e.printStackTrace();
        }
    }


    /**
     * 微信公众号登录授权回调uri
     *
     * @param accessId
     * @param authCode
     * @param expiresIn
     * @return
     */
    @RequestMapping("/{accessId}/authcallback")
    @ResponseBody
    public Result authcallback(@PathVariable("accessId") String accessId,
                               @RequestParam("auth_code") String authCode,
                               @RequestParam("expires_in") String expiresIn) {

        Result result = Result.success();
        if (!StringUtils.isNotBlank(authCode)) {
            logger.error("authcallback#exception:", new Exception("The authorization code is empty"));
            result = Result.failure();
            result.setInfo("The authorization code is empty.");
            return result;
        }
        try {
            OpenConfig openConfig = openService.getOpenConfig(authCode, expiresIn, accessId);
            if (openConfig != null) {
                logger.info("authcallback#appid:" + openConfig.getAppid());
                WxPlatformInfo wxPlatformInfo = openService.apiGetAuthorizerInfo(openConfig.getAppid(), accessId);
                if (wxPlatformInfo != null) {
                    String pre_auth_code = openService.getPreAuthCodeCache();
                    if (pre_auth_code != null) {
                        RedisTemplateUtil.del(WxRedisConstant.OPEN_PRE_AUTH_CODE);
                    }
                    String redurl = RedisTemplateUtil.get(WxRedisConstant.OPEN_ACCESS_REDURL + accessId).toString();
                    redurl = redurl + "?uuid=" + accessId + "&openid=" + wxPlatformInfo.getUserName() + "&appid=" + wxPlatformInfo.getAppid();
                    String result1 = HttpClientUtils.get(redurl);
                    logger.info("authcallback#result1:" + result1);
                } else {
                    result = Result.failure();
                    result.setInfo("The authorizerInfo is empty.");
                    return result;
                }
            }
        } catch (Exception e) {
            logger.error("accessId:" + accessId + "==>authcallback#exception:", e);
            e.printStackTrace();
            result = Result.failure();
        }
        return result;
    }

}


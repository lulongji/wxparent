package com.yuntongxun.service.open.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.base.wechat.util.CommonUtil;
import com.yuntongxun.base.wechat.util.HttpClientUtils;
import com.yuntongxun.dao.open.OpenConfigDao;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.constants.WxRedisConstant;
import com.yuntongxun.model.po.open.OpenConfig;
import com.yuntongxun.model.po.open.WxPlatformInfo;
import com.yuntongxun.model.properties.WxOpenProperties;
import com.yuntongxun.model.wechat.open.AuthorizerAccessToken;
import com.yuntongxun.model.wechat.open.ComponentToken;
import com.yuntongxun.model.wechat.open.PreAuthCode;
import com.yuntongxun.service.open.OpenService;
import com.yuntongxun.service.open.WxOpenService;
import com.yuntongxun.service.open.WxPlatformInfoService;
import com.yuntongxun.utils.WxOpenUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lu
 */
@Service
public class OpenServiceImpl implements OpenService {

    private static final Logger logger = LoggerFactory.getLogger(OpenServiceImpl.class);

    @Autowired
    private OpenConfigDao openConfigDao;

    @Autowired
    private WxOpenService wxOpenService;

    @Autowired
    private WxOpenProperties wxOpenProperties;

    @Autowired
    private WxPlatformInfoService wxPlatformInfoService;

    @Override
    public OpenConfig getOpenConfigByAppId(String appid) throws Exception {
        OpenConfig openConfig = new OpenConfig();
        openConfig.setAppid(appid);
        return openConfigDao.getOpenConfigByAppId(openConfig);
    }

    @Override
    public OpenConfig getOpenConfig(String authCode, String expiresIn, String accessId) throws Exception {
        Object componentAccessToken = RedisTemplateUtil.get(WxRedisConstant.OPEN_COMPONENT_ACCESS_TOKEN);
        if (null != componentAccessToken) {
            String url = WxConstant.API_QUERY_AUTH.replace("[COMPONENT_ACCESS_TOKEN]", componentAccessToken.toString());
            String jsonObject = HttpClientUtils.post(url, getApiQueryAuthJsondata(authCode));
            if (null != jsonObject) {
                JSONObject jsonData = JSON.parseObject(jsonObject);
                if (jsonData.containsKey("authorization_info")) {
                    JSONObject authorization_info = jsonData.getJSONObject("authorization_info");
                    String authorizer_appid = authorization_info.getString("authorizer_appid");
                    String authorizer_access_token = authorization_info.getString("authorizer_access_token");
                    String expires_in = authorization_info.getString("expires_in");
                    String authorizer_refresh_token = authorization_info.getString("authorizer_refresh_token");
                    StringBuilder func_info = new StringBuilder();
                    JSONArray jarr = authorization_info.getJSONArray("func_info");
                    for (int i = 0; i < jarr.size(); i++) {
                        func_info.append(jarr.getJSONObject(i).getJSONObject("funcscope_category").getString("id")).append("|");
                    }
                    OpenConfig openConfigBean = new OpenConfig();
                    openConfigBean.setAccessId(accessId);
                    openConfigBean.setAuthCode(authCode);
                    openConfigBean.setAuthCodeExpiresIn(expiresIn);
                    openConfigBean.setAuthCodeTime(System.currentTimeMillis() + "");
                    openConfigBean.setAppid(authorizer_appid);
                    openConfigBean.setAuthorizerRefreshToken(authorizer_refresh_token);
                    openConfigBean.setAuthorizerAccessToken(authorizer_access_token);
                    openConfigBean.setAuthorizerAccessTokenExpiresIn(expires_in);
                    openConfigBean.setAuthorizerAccessTokenTime(System.currentTimeMillis() + "");
                    openConfigBean.setFuncInfo(func_info.toString());
                    OpenConfig openConfig = getOpenConfigByAppId(authorizer_appid);
                    if (null != openConfig) {
                        logger.info("getOpenConfig#update success. openid:" + authorizer_appid);
                        wxOpenService.updateByAppId(openConfigBean);
                    } else {
                        wxOpenService.add(openConfigBean);
                        logger.info("getOpenConfig#insert success. openid:" + authorizer_appid);
                    }
                    return openConfigBean;
                } else {
                    logger.error("getOpenConfig-->api_query_auth->authorization_info#excepion:", new Exception("authorization_info is null!"));
                }
            } else {
                logger.error("getOpenConfig-->api_query_auth-->authorization_info#exception:", new Exception("Use authorization code to exchange the authorization information of the public number failed, the return information is empty."));
            }
        } else {
            logger.error("getOpenConfig-->api_query_auth#exception", new Exception("Failed to exchange authorization information of public number with authorization code, component_access_token is empty."));
        }

        return null;
    }

    @Override
    public String getComponentAccessToken() throws Exception {
        String componentAccessToken;
        Object token = RedisTemplateUtil.get(WxRedisConstant.OPEN_COMPONENT_ACCESS_TOKEN);
        if (token == null) {
            componentAccessToken = getAccessToken();
            RedisTemplateUtil.set(WxRedisConstant.OPEN_COMPONENT_ACCESS_TOKEN, componentAccessToken, 7000, TimeUnit.SECONDS);
        } else {
            componentAccessToken = token.toString();
        }
        return componentAccessToken;
    }

    @Override
    public String getPreAuthCodeCache() throws Exception {
        String preAuthCode;
        Object o = RedisTemplateUtil.get(WxRedisConstant.OPEN_PRE_AUTH_CODE);
        if (o == null) {
            preAuthCode = getPreAuthCode();
            RedisTemplateUtil.set(WxRedisConstant.OPEN_PRE_AUTH_CODE, preAuthCode, 500, TimeUnit.SECONDS);
        } else {
            preAuthCode = o.toString();
        }
        return preAuthCode;
    }

    @Override
    public String getAuthorizerAccessToken(String appid) throws Exception {
        String authorizerAccessToken;
        Object o = RedisTemplateUtil.get(WxRedisConstant.AUTHORIZER_ACCESS_TOKEN + appid);
        if (o == null) {
            authorizerAccessToken = getAuthorizerAccessTokenToWx(appid);
            RedisTemplateUtil.set(WxRedisConstant.AUTHORIZER_ACCESS_TOKEN + appid, authorizerAccessToken, 70, TimeUnit.MINUTES);
        } else {
            authorizerAccessToken = o.toString();
        }
        return authorizerAccessToken;
    }

    /**
     * 微信服务器获取预授权码
     *
     * @return
     * @throws Exception
     */
    private String getPreAuthCode() throws Exception {
        String preAuthCode = null;
        PreAuthCode p = getPreAuthCodeToWx(getPreAuthCodeJsonData());
        if (p != null) {
            preAuthCode = p.getPreAuthCode();
        }
        return preAuthCode;
    }

    /**
     * 获取预授权码
     *
     * @param jsondata
     * @return
     */
    public PreAuthCode getPreAuthCodeToWx(String jsondata) {
        PreAuthCode preAuthCode = null;
        try {
            String uri = WxConstant.PRE_AUTH_CODE_URI.replace("[COMPONENT_ACCESS_TOKEN]", getComponentAccessToken());
            JSONObject jsonObject = CommonUtil.httpsRequest(uri, "POST", jsondata);
            if (null != jsonObject) {
                preAuthCode = new PreAuthCode();
                preAuthCode.setPreAuthCode(jsonObject.getString("pre_auth_code"));
                preAuthCode.setExpiresIn(jsonObject.getString("expires_in"));
            }
        } catch (Exception e) {
            logger.error("getPreAuthCode#exception:", e);
            e.printStackTrace();
        }
        return preAuthCode;
    }

    /**
     * 拼接开放平台预授权码参数
     *
     * @return
     * @throws Exception
     */
    private String getPreAuthCodeJsonData() throws Exception {
        JSONObject jsondata = new JSONObject();
        jsondata.put("component_appid", wxOpenProperties.getComponentAppId());
        return jsondata.toString();
    }


    /**
     * 直接请求微信端ComponentAccessToken
     *
     * @return
     * @throws Exception
     */
    private String getAccessToken() throws Exception {
        String componentAccessToken = null;
        ComponentToken componentToken = WxOpenUtil.getComponentccessToken(getJsonData());
        if (componentToken != null) {
            componentAccessToken = componentToken.getComponentAccessToken();
        }
        return componentAccessToken;

    }

    /**
     * 拼接开放平台获取token参数
     *
     * @return
     * @throws Exception
     */
    private String getJsonData() throws Exception {
        Object ticket = RedisTemplateUtil.get(WxRedisConstant.OPEN_COMPONENT_VERIFY_TICKET);
        JSONObject jsondata = new JSONObject();
        jsondata.put("component_appid", wxOpenProperties.getComponentAppId());
        jsondata.put("component_appsecret", wxOpenProperties.getComponentSecret());
        jsondata.put("component_verify_ticket", ticket.toString());
        return jsondata.toString();
    }


    /**
     * 拼接开放平台获取接口调用凭据和授权信息参数信息
     *
     * @param authCode
     * @return
     * @throws Exception
     */
    private String getApiQueryAuthJsondata(String authCode) throws Exception {
        JSONObject jsondata = new JSONObject();
        jsondata.put("component_appid", wxOpenProperties.getComponentAppId());
        jsondata.put("authorization_code", authCode);
        return jsondata.toString();
    }


    /**
     * 直接获取微信端 authorizerAccessToken
     *
     * @param appid
     * @return
     * @throws Exception
     */
    private String getAuthorizerAccessTokenToWx(String appid) throws Exception {
        String authorizerAccessToken = null;
        AuthorizerAccessToken token = WxOpenUtil.getAuthorizerAccessTokenToWx(getAuthorizerAccessTokenJson(appid));
        if (token != null) {
            authorizerAccessToken = token.getAuthorizerAccessToken();
            String authorizerRefreshToken = token.getAuthorizerRefreshToken();
            OpenConfig openConfig = new OpenConfig();
            openConfig.setAppid(appid);
            openConfig.setAuthorizerAccessToken(authorizerAccessToken);
            openConfig.setAuthorizerAccessTokenTime(System.currentTimeMillis() + "");
            openConfig.setAuthorizerRefreshToken(authorizerRefreshToken);
            wxOpenService.updateByAppId(openConfig);
        }
        return authorizerAccessToken;

    }


    /**
     * authorizerAccessToken的参数拼接
     *
     * @param appid
     * @return
     * @throws Exception
     */
    @Override
    public String getAuthorizerAccessTokenJson(String appid) throws Exception {
        OpenConfig openConfig = getOpenConfigByAppId(appid);
        if (openConfig == null) {
            return null;
        }
        JSONObject jsondata = new JSONObject();
        jsondata.put("component_appid", wxOpenProperties.getComponentAppId());
        jsondata.put("authorizer_appid", appid);
        jsondata.put("authorizer_refresh_token", openConfig.getAuthorizerRefreshToken());
        return jsondata.toString();
    }

    @Override
    public WxPlatformInfo apiGetAuthorizerInfo(String authorizerAppid, String accessId) throws Exception {
        String uri = WxConstant.API_GET_AUTHORIZER_INFO_URL.replace("[COMPONENT_ACCESS_TOKEN]", getComponentAccessToken());
        JSONObject jsonObject = CommonUtil.httpsRequest(uri, "POST", apiGetAuthorizerInfoJsondata(authorizerAppid));
        if (null != jsonObject) {
            if (jsonObject.containsKey("authorizer_info")) {
                JSONObject authorizer_info = jsonObject.getJSONObject("authorizer_info");
                String nick_name = authorizer_info.getString("nick_name");
                String head_img = authorizer_info.getString("head_img");
                String user_name = authorizer_info.getString("user_name");
                String alias = authorizer_info.getString("alias");

                WxPlatformInfo wp = new WxPlatformInfo();
                wp.setAppid(authorizerAppid);
                WxPlatformInfo wxPlatformInfo = wxPlatformInfoService.get(wp);


                wp.setAlias(alias);
                wp.setHeadImg(head_img);
                wp.setUserName(user_name);
                wp.setNickName(nick_name);
                wp.setCreatetime(System.currentTimeMillis() + "");
                wp.setRonglianappid(accessId);
                if (wxPlatformInfo == null) {
                    wxPlatformInfoService.add(wp);
                } else {
                    wxPlatformInfoService.updateByAppid(wp);
                }


            }
        } else {
            logger.error("apiGetAuthorizerInfo -->  Failed to get the authorized party's account information, the request result is empty.  authorizerAppid=" + authorizerAppid);
        }
        return null;
    }


    /**
     * 获取授权方的帐号基本信息Json参数
     *
     * @param authorizer_appid
     * @return
     * @throws Exception
     */
    private String apiGetAuthorizerInfoJsondata(String authorizer_appid) throws Exception {
        JSONObject jsondata = new JSONObject();
        jsondata.put("component_appid", wxOpenProperties.getComponentAppId());
        jsondata.put("authorizer_appid", authorizer_appid);
        return jsondata.toString();
    }


}

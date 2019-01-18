package com.yuntongxun.controller;

import com.yuntongxun.base.constants.CommonConstants;
import com.yuntongxun.base.wechat.WxType;
import com.yuntongxun.base.wechat.util.XmlUtil;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.po.open.OpenConfig;
import com.yuntongxun.model.properties.WxOpenProperties;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.service.message.WxMessageService;
import com.yuntongxun.service.open.OpenService;
import com.yuntongxun.service.open.WxOpenComponentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


/**
 * @Description: 微信开放平台
 * @Author: lulongji
 * @Date: Created in 15:23 2018/9/29
 */

@RestController
@RequestMapping("/open")
public class OpenNotifyController {

    private static final Logger logger = LoggerFactory.getLogger(OpenNotifyController.class);

    @Autowired
    private WxOpenProperties wxOpenProperties;

    @Autowired
    private WxOpenComponentService wxOpenComponentService;

    @Autowired
    private OpenService openService;

    @Autowired
    private WxMessageService wxMessageService;


    /**
     * 授权通知
     *
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @param signature
     * @param encType
     * @return
     */
    @RequestMapping("/notify")
    public String notifyAll(@RequestBody(required = false) String requestBody,
                            @RequestParam("timestamp") String timestamp,
                            @RequestParam("nonce") String nonce,
                            @RequestParam("msg_signature") String msgSignature,
                            @RequestParam("signature") String signature,
                            @RequestParam(name = "encrypt_type", required = false) String encType) {
        logger.info("Receive WeChat requests：[signature=[{}], encType=[{}], msgSignature=[{}], timestamp=[{}], nonce=[{}]",
                signature, encType, msgSignature, timestamp, nonce);

        if (!StringUtils.equalsIgnoreCase(WxConstant.WX_AES, encType)
                || !wxOpenComponentService.checkSignature(wxOpenProperties.getComponentToken(), timestamp, nonce, signature)) {
            throw new IllegalArgumentException("Illegal requests, possibly bogus requests!");
        }
        try {
            String xmlStr = CommonUtils.msgDecrypt(requestBody, timestamp, nonce, msgSignature, encType,
                    wxOpenProperties.getComponentAppId(),
                    wxOpenProperties.getComponentToken(),
                    wxOpenProperties.getComponentAesKey());

            logger.debug("\nThe content of the decrypted message is：\n{} ", xmlStr);
            wxOpenComponentService.wxRoute(xmlStr);
        } catch (Exception e) {
            logger.error("Decryption abnormal,e:", e);
            return CommonConstants.ValType.FAILURE_INFO;
        }
        return CommonConstants.ValType.SUCCESS_INFO;
    }

    /**
     * 微信第三方平台中的公众号消息与事件接收
     *
     * @param appid
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @return
     */
    @RequestMapping("/{appid}/callback")
    public String callback(@RequestBody(required = false) String requestBody,
                           @PathVariable("appid") String appid,
                           @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce,
                           @RequestParam("signature") String signature,
                           @RequestParam("msg_signature") String msgSignature,
                           @RequestParam(name = "encrypt_type", required = false) String encType) {

        logger.info("Receive WeChat requests：[appid=[{}], timestamp=[{}], nonce=[{}], msg_signature=[{}], encType=[{}], signature=[{}]",
                appid, timestamp, nonce, msgSignature, encType, signature);

        if (!StringUtils.equalsIgnoreCase(WxConstant.WX_AES, encType)
                || !wxOpenComponentService.checkSignature(wxOpenProperties.getComponentToken(), timestamp, nonce, signature)) {
            throw new IllegalArgumentException("Illegal requests, possibly bogus requests!");
        }
        try {
            String xmlStr = CommonUtils.msgDecrypt(requestBody, timestamp, nonce, msgSignature, encType,
                    wxOpenProperties.getComponentAppId(),
                    wxOpenProperties.getComponentToken(),
                    wxOpenProperties.getComponentAesKey());

            logger.debug("\nThe content of the decrypted message is：\n{} ", xmlStr);
            Map<String, String> map = XmlUtil.xml2map(xmlStr);
            map.put("ronglianappid", getAccessId(appid));
            map.put("sendMcmMsgUrl", wxOpenProperties.getSendMcmMsgUrl());
            map.put("appid", appid);
            map.put("WxType", WxType.Open.toString());
            //TODO 处理公众号渠道信息交互
            wxMessageService.doMessage(map);


        } catch (Exception e) {
            logger.error("Decryption abnormal,e:", e);
        }
        return CommonConstants.ValType.SUCCESS_INFO;
    }


    /**
     * 获取ronglianappid
     *
     * @param appid
     * @return
     * @throws Exception
     */
    private String getAccessId(String appid) throws Exception {
        OpenConfig openConfig = openService.getOpenConfigByAppId(appid);
        if (null == openConfig) {
            logger.info("This user information is not available------>appid:" + appid);
            return null;
        }
        return openConfig.getAccessId();
    }
}


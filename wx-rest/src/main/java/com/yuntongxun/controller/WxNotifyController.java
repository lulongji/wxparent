package com.yuntongxun.controller;

import com.yuntongxun.base.wechat.WxType;
import com.yuntongxun.base.wechat.util.SignUtil;
import com.yuntongxun.base.wechat.util.XmlUtil;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.properties.WxProperties;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.service.message.WxMessageService;
import com.yuntongxun.service.wechat.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Description: 微信公众平台
 * @Author: lulongji
 * @Date: Created in 15:23 2018/9/29
 */

@RestController
@RequestMapping("/wx")
public class WxNotifyController {

    private static final Logger logger = LoggerFactory.getLogger(WxNotifyController.class);


    @Autowired
    private WechatService wechatService;

    @Autowired
    private WxMessageService wxMessageService;

    @Autowired
    private WxProperties wxProperties;

    /**
     * WeChat service get.
     *
     * @param request
     * @param response
     * @param appid
     * @throws Exception
     */
    @RequestMapping(value = "/{appid}/process", produces = "application/json;charset=UTF-8", method = RequestMethod.GET)
    public void processGet(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable("appid") String appid) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            PrintWriter out = response.getWriter();
            String signature = request.getParameter("signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String echostr = request.getParameter("echostr");
            String token = wechatService.getToken(appid);

            logger.info("Enter the WeChat service...get...appid:" + appid + ",signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",token:" + token);
            if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
                out.print(echostr);
            }

            out.close();
        } catch (Exception e) {
            logger.error("WeChat service get exception:", e);
            e.printStackTrace();
        }
    }

    /**
     * WeChat service post.
     *
     * @param request
     * @param response
     * @param appid
     * @throws Exception
     */
    @RequestMapping(value = "/{appid}/process", produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public void processPost(HttpServletRequest request, HttpServletResponse response, @PathVariable("appid") String appid) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            String encryptType = request.getParameter("encrypt_type");
            String signature = request.getParameter("signature");
            String msgSignature = request.getParameter("msg_signature");
            String timestamp = request.getParameter("timestamp");
            String nonce = request.getParameter("nonce");
            String token = wechatService.getToken(appid);

            logger.info("Enter the WeChat service...post...appid:" + appid + ",encryptType:" + encryptType + ",signature:" + signature + ",msgSignature:" + msgSignature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",token:" + token);
            if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
                Map<String, String> requestMap;
                String retstr = CommonUtils.getRequestStr(request);
                logger.info("=======>WeChat request data:" + retstr);
                if (WxConstant.WX_AES.equals(encryptType)) {
                    logger.info("=======>WeChat service aes:" + appid);
                    String encrypt = CommonUtils.getXmlNode(retstr, "Encrypt");
                    String encodingAESKey = wechatService.getEncodingAESKey(appid);

                    String xmlStr = CommonUtils.msgDecrypt(timestamp, nonce, msgSignature, encrypt, appid, token, encodingAESKey);
                    requestMap = XmlUtil.xml2map(xmlStr);
                } else {
                    logger.info("=======>WeChat service unencrypted:" + appid);
                    requestMap = XmlUtil.xml2map(new String(retstr.getBytes(), "UTF-8"));
                }
                String appsecret = wechatService.getAppSecret(appid);
                String ronglianappid = wechatService.getRongLianAppid(appid);
                requestMap.put("ronglianappid", ronglianappid);
                requestMap.put("sendMcmMsgUrl", wxProperties.getSendMcmMsgUrl());
                requestMap.put("appid", appid);
                requestMap.put("appsecret", appsecret);
                requestMap.put("WxType", WxType.MP.toString());
                wxMessageService.doMessage(requestMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("WeChat service post exception:", e);
        }
    }
}

package com.yuntongxun.controller;

import com.yuntongxun.base.webchat.SignUtil;
import com.yuntongxun.base.webchat.XmlUtil;
import com.yuntongxun.model.wechat.utils.MessageUtil;
import com.yuntongxun.model.wechat.utils.WXUtils;
import com.yuntongxun.utils.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Description: 微信公众平台
 * @Author: lulongji
 * @Date: Created in 15:23 2018/9/29
 */

@RestController
@RequestMapping("/webchat")
public class WebchatController {

    private static final Logger logger = LoggerFactory.getLogger(WebchatController.class);


    /**
     * WeChat service get.
     *
     * @param request
     * @param response
     * @param appid
     * @throws Exception
     */
    @RequestMapping(value = "/{appid}/process", produces = "application/json;charset=UTF-8")
    public void processGet(HttpServletRequest request, HttpServletResponse response, @PathParam("appid") String appid) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        PrintWriter out = response.getWriter();
        // 调用核心业务类接收消息、处理消息
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");
        //token
        String token = WeChatUtil.getAccessToken(appid);

        logger.info("Enter the WeChat service...get...appid:" + appid + ",signature:" + signature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",token:" + token);
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
            out.print(echostr);
        }

        out.close();
        out = null;
    }

    /**
     * WeChat service post.
     *
     * @param request
     * @param response
     * @param appid
     * @throws Exception
     */
    @RequestMapping(value = "/{appid}/process", produces = "application/json;charset=UTF-8")
    public void processPost(HttpServletRequest request, HttpServletResponse response, @PathParam("appid") String appid) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String encryptType = request.getParameter("encrypt_type");
        // 微信加密签名
        String signature = request.getParameter("signature");
        // 微信加密签名
        String msgSignature = request.getParameter("msg_signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        //TODO token
        String token = "";

        String respMessage = null;

        logger.info("Enter the WeChat service...post...appid:" + appid + ",encryptType:" + encryptType + ",signature:" + signature + ",msgSignature:" + msgSignature + ",timestamp:" + timestamp + ",nonce:" + nonce + ",token:" + token);
        try {
            PrintWriter out = response.getWriter();
            // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
            if (SignUtil.checkSignature(signature, timestamp, nonce, token)) {
                Map<String, String> requestMap = null;
                String retstr = MessageUtil.getRequestStr(request);
                logger.info("=======>WeChat request data:" + retstr);
                if ("aes".equals(encryptType)) {
                    logger.info("=======>WeChat service aes:" + appid);
                    String encrypt = WXUtils.getXmlNode(retstr, "Encrypt");
                    //TODO  encodingAESKey
                    String encodingAESKey = "";
                    //getEncodingAESKey(appid);//消息加解密密钥
                    String xmlStr = WXUtils.msgDecrypt(timestamp, nonce, msgSignature, encrypt, appid, token, encodingAESKey);
                    requestMap = XmlUtil.xml2map(xmlStr);
                } else {
                    logger.info("=======>WeChat service unencrypted:" + appid);
                    requestMap = XmlUtil.xml2map(new String(retstr.getBytes(), "UTF-8"));
                }
            }
            if (null != respMessage)
                out.print(respMessage);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

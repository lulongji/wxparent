package com.yuntongxun.service.open.impl;


import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.base.wechat.util.aes.SHA1;
import com.yuntongxun.model.constants.WxRedisConstant;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.service.open.WxOpenComponentService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author lu
 */
@Service
public class WxOpenComponentServiceImpl implements WxOpenComponentService {

    private static final Logger logger = LoggerFactory.getLogger(WxOpenComponentServiceImpl.class);

    @Override
    public boolean checkSignature(String component_token, String timestamp, String nonce, String signature) {
        try {
            return SHA1.gen(new String[]{component_token, timestamp, nonce}).equals(signature);
        } catch (Exception e) {
            logger.error("Checking signature failed, and the reason is :" + e.getMessage(), e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void wxRoute(String xmlStr) throws Exception {
        if (xmlStr == null) {
            throw new NullPointerException("message is empty");
        } else {
            String infoType = CommonUtils.getXmlNode(xmlStr, "InfoType");
            if (StringUtils.equalsIgnoreCase(infoType, "component_verify_ticket")) {
                logger.info("WeChat bill information push...:");
                String ticketStr = CommonUtils.getXmlNode(xmlStr, "ComponentVerifyTicket");
                logger.debug("缓存票据信息,ticketStr:" + ticketStr);
                RedisTemplateUtil.set(WxRedisConstant.OPEN_COMPONENT_VERIFY_TICKET, ticketStr, 0, TimeUnit.DAYS);
            } else if (StringUtils.equalsIgnoreCase(infoType, "unauthorized")) {
                //TODO 日志记录
                logger.info("WeChat cancel authorization notice...");
                String authorizerAppid = CommonUtils.getXmlNode(xmlStr, "AuthorizerAppid");

            } else if (StringUtils.equalsIgnoreCase(infoType, "authorized")) {
                logger.info("WeChat authorization successful notification...");
                //TODO 日志记录

            } else if (StringUtils.equalsIgnoreCase(infoType, "updateauthorized")) {
                logger.info("WeChat updates authorization notifications...");
                //TODO 日志记录
                String authorizationcode = CommonUtils.getXmlNode(xmlStr, "AuthorizationCode");

            } else {
            }

        }
    }

}

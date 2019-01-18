package com.yuntongxun.service.message;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.constants.CommonConstants;
import com.yuntongxun.base.wechat.util.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;

/**
 * @Description:
 * @Author: lu
 * @Date: Created in 12:24 2018/11/17
 */
@Service
public class WxMessageHandel {

    private static final Logger logger = LoggerFactory.getLogger(WxMessageHandel.class);


    /**
     * 发送消息（三次放弃）
     *
     * @param jsonData
     * @param sendMcmMsgUrl
     */
    public synchronized void sendMsg(String jsonData, String sendMcmMsgUrl) {
        try {
            if (push(jsonData, sendMcmMsgUrl)) {
                logger.info("push msg zx to the 3rd party ：num=1");
            } else {
                Thread.sleep(2000);
                if (push(jsonData, sendMcmMsgUrl)) {
                    logger.info("push msg zx to the 3rd party：num=2");
                } else {
                    Thread.sleep(2000);
                    if (push(jsonData, sendMcmMsgUrl)) {
                        logger.info("push msg zx to the 3rd party：num=3");
                    } else {
                        logger.info("push 3 times,all failed,over. sendMcmMsgUrl:" + sendMcmMsgUrl);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Abnormal results:", e);
            e.printStackTrace();
        }
    }


    /**
     * 向mcm发送消息
     *
     * @param sendData
     * @param sendMcmMsgUrl
     * @return
     */
    private boolean push(String sendData, String sendMcmMsgUrl) {
        try {
            String result = HttpClientUtils.post(sendMcmMsgUrl, sendData);
            if (result != null) {
                HashMap<String, Object> resultMap = JSONObject.parseObject(result, HashMap.class);
                if ((Boolean) resultMap.get(CommonConstants.ValType.SUCCESS_INFO)) {
                    return true;
                } else {
                    return false;
                }
            }
            return false;
        } catch (Exception e) {
            logger.error("Push mcm exception:", e);
            e.printStackTrace();
            return false;
        }
    }


}

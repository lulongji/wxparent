package com.yuntongxun.service.open;


import com.yuntongxun.base.wechat.error.WxErrorException;

/**
 * @author lu
 */
public interface WxOpenComponentService {

    /**
     * 校验
     *
     * @param component_token
     * @param timestamp
     * @param nonce
     * @param signature
     * @return
     */
    boolean checkSignature(String component_token, String timestamp, String nonce, String signature);


    /**
     * 消息路由
     *
     * @param xmlStr
     * @return
     * @throws WxErrorException
     */
    void wxRoute(String xmlStr) throws Exception;

}

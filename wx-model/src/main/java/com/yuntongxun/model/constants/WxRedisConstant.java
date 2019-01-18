package com.yuntongxun.model.constants;

/**
 * @Description:wechat constant.
 * @Author: lu
 * @Date: Created in 18:44 2018/11/12
 */
public interface WxRedisConstant {

    /**
     * 微信基础配置
     */
    String WX_HEAD_KEY = "ytx500|wx|";

    /**
     * 微信公众平台Token + appid
     */
    String WX_TOKEN = WX_HEAD_KEY + "token" + "|";

    /**
     * 微信公众平台encodingAESKey + appid
     */
    String WX_ENCODINGAES_KEY = WX_HEAD_KEY + "encodingaeskey" + "|";

    /**
     * 微信公众平台appSecret + appid
     */
    String WX_APPSECRET = WX_HEAD_KEY + "appsecret" + "|";

    /**
     * 微信公众平台 accesstoken + appid
     */
    String WX_ACCESSTOKEN = WX_HEAD_KEY + "accesstoken|";

    /**
     * 微信公众平台ronglianappid + appid
     */
    String WX_RONGLIANAPPID = WX_HEAD_KEY + "ronglianappid|";


    /**
     * 微信开放平台基础配置
     */
    String WX_OPEN_HEAD_KEY = "ytx600|wx|";

    /**
     * 微信开放平台票据信息ComponentVerifyTicket
     */
    String OPEN_COMPONENT_VERIFY_TICKET = WX_OPEN_HEAD_KEY + "ComponentVerifyTicket";

    /**
     * 微信开放平台票据信息ComponentAccessToken
     */
    String OPEN_COMPONENT_ACCESS_TOKEN = WX_OPEN_HEAD_KEY + "ComponentAccessToken";

    /**
     * 微信开放平台获取预授权码pre_auth_code
     */
    String OPEN_PRE_AUTH_CODE = WX_OPEN_HEAD_KEY + "PreAuthCode";

    /**
     * 微信开放平台redurl
     */
    String OPEN_ACCESS_REDURL = WX_OPEN_HEAD_KEY + "AccessId|";

    /**
     * 微信开放平台AuthorizerAccessToken
     */
    String AUTHORIZER_ACCESS_TOKEN = WX_OPEN_HEAD_KEY + "AuthorizerAccessToken|";

}

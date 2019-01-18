package com.yuntongxun.service.open;

import com.yuntongxun.model.po.open.OpenConfig;
import com.yuntongxun.model.po.open.WxPlatformInfo;

/**
 * @Description: 第三方平台
 * @Author: lulongji
 * @Date: Created in 18:09 2018/11/13
 */
public interface OpenService {


    /**
     * 根据appid获取开放平台配置
     *
     * @param appid
     * @return
     * @throws Exception
     */
    OpenConfig getOpenConfigByAppId(String appid) throws Exception;


    /**
     * 获取第三方平台基础配置信息
     *
     * @param authCode
     * @param expiresIn
     * @param accessId
     * @return
     * @throws Exception
     */
    OpenConfig getOpenConfig(String authCode, String expiresIn, String accessId) throws Exception;


    /**
     * 获取ComponentAccessToken
     *
     * @return
     * @throws Exception
     */
    String getComponentAccessToken() throws Exception;


    /**
     * 获取预授权码
     *
     * @return
     * @throws Exception
     */
    String getPreAuthCodeCache() throws Exception;


    /**
     * 获取AuthorizerAccessToken
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getAuthorizerAccessToken(String appid) throws Exception;

    /**
     * 获取AuthorizerAccessTokenJson 拼接串
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getAuthorizerAccessTokenJson(String appid) throws Exception;

    /**
     * 获取授权方的账户信息
     *
     * @param authorizerAppid 授权方appid
     * @return
     */
    WxPlatformInfo apiGetAuthorizerInfo(String authorizerAppid, String accessId) throws Exception;

}

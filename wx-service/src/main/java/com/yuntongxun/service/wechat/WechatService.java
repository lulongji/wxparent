package com.yuntongxun.service.wechat;

import com.yuntongxun.model.po.wx.BaseConfig;

import java.util.List;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 15:43 2018/11/12
 */
public interface WechatService {

    /**
     * 根据appid查询微信基础配置
     *
     * @param baseConfig
     * @return
     * @throws Exception
     */
    BaseConfig getWxBaseConfig(BaseConfig baseConfig) throws Exception;

    /**
     * 获取微信基础配置
     *
     * @param baseConfig
     * @return
     * @throws Exception
     */
    List<BaseConfig> getWxBaseConfigList(BaseConfig baseConfig) throws Exception;


    /**
     * 根据appid 获取token
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getToken(String appid) throws Exception;


    /**
     * 根据appid 获取EncodingAESKey
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getEncodingAESKey(String appid) throws Exception;

    /**
     * 根据appid 获取 AppSecret
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getAppSecret(String appid) throws Exception;

    /**
     * 根据appid 获取 ronglianappid
     *
     * @param appid
     * @return
     * @throws Exception
     */
    String getRongLianAppid(String appid) throws Exception;


}

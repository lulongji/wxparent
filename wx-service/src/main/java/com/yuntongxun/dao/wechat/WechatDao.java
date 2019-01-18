package com.yuntongxun.dao.wechat;

import com.yuntongxun.model.po.wx.BaseConfig;

import java.util.List;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 15:43 2018/11/12
 */
public interface WechatDao {

    BaseConfig getWxBaseConfig(BaseConfig baseConfig) throws Exception;

    List<BaseConfig> getWxBaseConfigList(BaseConfig baseConfig) throws Exception;

}

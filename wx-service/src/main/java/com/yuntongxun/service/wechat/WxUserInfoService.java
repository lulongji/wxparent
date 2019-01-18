package com.yuntongxun.service.wechat;

import com.yuntongxun.model.po.wx.WxUserInfo;

import java.util.List;

/**
 * @Description: 微信关注用户基础数据信息
 * @Author: lulongji
 * @Date: Created in 19:39 2018/11/26
 */
public interface WxUserInfoService {

    void addWxUserInfo() throws Exception;

    void delWxUserInfo() throws Exception;

    void modifyWxUserInfo() throws Exception;

    WxUserInfo getWxUserInfo(WxUserInfo WxUserInfo) throws Exception;

    List<WxUserInfo> getWxUserInfoList(WxUserInfo wxUserInfo) throws Exception;

    void addWxUserInfo(WxUserInfo wxUserInfo) throws Exception;
}

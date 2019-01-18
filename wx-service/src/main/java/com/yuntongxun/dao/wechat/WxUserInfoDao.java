package com.yuntongxun.dao.wechat;

import com.yuntongxun.model.po.wx.WxUserInfo;

import java.util.List;

public interface WxUserInfoDao {

    void addWxUserInfo() throws Exception;

    void delWxUserInfo() throws Exception;

    void modifyWxUserInfo() throws Exception;

    WxUserInfo getWxUserInfo(WxUserInfo WxUserInfo) throws Exception;

    List<WxUserInfo> getWxUserInfoList(WxUserInfo wxUserInfo) throws Exception;

    void addWxUserInfo(WxUserInfo wxUserInfo) throws Exception;

}

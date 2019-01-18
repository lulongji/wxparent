package com.yuntongxun.service.wechat.impl;

import com.yuntongxun.dao.wechat.WxUserInfoDao;
import com.yuntongxun.model.po.wx.WxUserInfo;
import com.yuntongxun.service.wechat.WxUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WxUserInfoServiceImpl implements WxUserInfoService {


    @Autowired
    private WxUserInfoDao wxUserInfoDao;

    @Override
    public void addWxUserInfo() throws Exception {

    }

    @Override
    public void delWxUserInfo() throws Exception {

    }

    @Override
    public void modifyWxUserInfo() throws Exception {

    }

    @Override
    public WxUserInfo getWxUserInfo(WxUserInfo wxUserInfo) throws Exception {
        return null;
    }

    @Override
    public List<WxUserInfo> getWxUserInfoList(WxUserInfo wxUserInfo) throws Exception {
        return null;
    }

    @Override
    public void addWxUserInfo(WxUserInfo wxUserInfo) throws Exception {
        wxUserInfoDao.addWxUserInfo(wxUserInfo);
    }
}

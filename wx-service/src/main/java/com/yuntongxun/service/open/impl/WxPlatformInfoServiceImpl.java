package com.yuntongxun.service.open.impl;

import com.yuntongxun.dao.open.WxPlatformInfoDao;
import com.yuntongxun.model.po.open.WxPlatformInfo;
import com.yuntongxun.service.open.WxPlatformInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lu on 2018/12/20.
 */
@Service
public class WxPlatformInfoServiceImpl implements WxPlatformInfoService {


    @Autowired
    private WxPlatformInfoDao wxPlatformInfoDao;


    @Override
    public void add(WxPlatformInfo wxPlatformInfo) throws Exception {
        wxPlatformInfoDao.add(wxPlatformInfo);
    }

    @Override
    public void update(WxPlatformInfo wxPlatformInfo) throws Exception {
        wxPlatformInfoDao.update(wxPlatformInfo);
    }

    @Override
    public void updateByAppid(WxPlatformInfo wxPlatformInfo) throws Exception {
        wxPlatformInfoDao.updateByAppid(wxPlatformInfo);
    }

    @Override
    public void delete(WxPlatformInfo wxPlatformInfo) throws Exception {
        wxPlatformInfoDao.delete(wxPlatformInfo);
    }

    @Override
    public WxPlatformInfo get(WxPlatformInfo wxPlatformInfo) throws Exception {
        if (wxPlatformInfoDao.getWxPlatformInfoList(wxPlatformInfo).size() > 0) {
            return wxPlatformInfoDao.getWxPlatformInfoList(wxPlatformInfo).get(0);
        } else {
            return null;
        }
    }

    @Override
    public List<WxPlatformInfo> getWxPlatformInfoList(WxPlatformInfo wxPlatformInfo) throws Exception {
        return wxPlatformInfoDao.getWxPlatformInfoList(wxPlatformInfo);
    }
}

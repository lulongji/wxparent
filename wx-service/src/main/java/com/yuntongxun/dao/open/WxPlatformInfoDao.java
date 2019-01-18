package com.yuntongxun.dao.open;

import com.yuntongxun.model.po.open.WxPlatformInfo;

import java.util.List;


/**
 * @author systemCode
 */
public interface WxPlatformInfoDao {

    void add(WxPlatformInfo wxPlatformInfo) throws Exception;

    void update(WxPlatformInfo wxPlatformInfo) throws Exception;

    void updateByAppid(WxPlatformInfo wxPlatformInfo) throws Exception;

    void delete(WxPlatformInfo wxPlatformInfo) throws Exception;

    List<WxPlatformInfo> getWxPlatformInfoList(WxPlatformInfo wxPlatformInfo) throws Exception;

}

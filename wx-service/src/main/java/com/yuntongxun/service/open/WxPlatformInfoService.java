package com.yuntongxun.service.open;

import com.yuntongxun.model.po.open.WxPlatformInfo;

import java.util.List;

/**
 * Created by lu on 2018/12/20.
 */
public interface WxPlatformInfoService {


    /**
     * 添加数据
     *
     * @param wxPlatformInfo
     * @return
     * @author System
     */
    void add(WxPlatformInfo wxPlatformInfo) throws Exception;

    /**
     * 修改数据
     *
     * @param wxPlatformInfo
     * @return
     * @author System
     */
    void update(WxPlatformInfo wxPlatformInfo) throws Exception;

    /**
     * @param wxPlatformInfo
     * @throws Exception
     */
    void updateByAppid(WxPlatformInfo wxPlatformInfo) throws Exception;

    /**
     * 删除数据
     *
     * @param wxPlatformInfo
     * @return
     * @author System
     */
    void delete(WxPlatformInfo wxPlatformInfo) throws Exception;

    /**
     * 查询单个数据
     *
     * @param wxPlatformInfo
     * @return
     * @author System
     */
    WxPlatformInfo get(WxPlatformInfo wxPlatformInfo) throws Exception;

    /**
     * 查询所有数据
     *
     * @param wxPlatformInfo
     * @return
     * @author System
     */
    List<WxPlatformInfo> getWxPlatformInfoList(WxPlatformInfo wxPlatformInfo) throws Exception;
}

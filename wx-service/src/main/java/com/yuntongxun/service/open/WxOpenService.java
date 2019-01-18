package com.yuntongxun.service.open;


import com.yuntongxun.model.po.open.OpenConfig;

import java.util.List;

/**
 * @author lu
 */
public interface WxOpenService {


    /**
     * 添加数据
     *
     * @param openConfig
     * @return
     * @author System
     */
    void add(OpenConfig openConfig) throws Exception;

    /**
     * 修改数据
     *
     * @param openConfig
     * @return
     * @author System
     */
    void updateByAppId(OpenConfig openConfig) throws Exception;

    /**
     * 删除数据
     *
     * @param openConfig
     * @return
     * @author System
     */
    void delete(OpenConfig openConfig) throws Exception;

    /**
     * 查询单个数据
     *
     * @param openConfig
     * @return
     * @author System
     */
    OpenConfig get(OpenConfig openConfig) throws Exception;

    /**
     * 查询所有数据
     *
     * @param openConfig
     * @return
     * @author System
     */
    List<OpenConfig> getOpenConfigList(OpenConfig openConfig) throws Exception;


}

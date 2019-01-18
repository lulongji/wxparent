package com.yuntongxun.dao.open;

import com.yuntongxun.model.po.open.OpenConfig;

import java.util.List;

/**
 * Created by lu on 2019/1/10.
 */
public interface OpenConfigDao {

    void add(OpenConfig openConfig) throws Exception;

    void updateByAppId(OpenConfig openConfig) throws Exception;

    void delete(OpenConfig openConfig) throws Exception;

    List<OpenConfig> getOpenConfigList(OpenConfig openConfig) throws Exception;

    OpenConfig getOpenConfigByAppId(OpenConfig openConfig) throws Exception;


}

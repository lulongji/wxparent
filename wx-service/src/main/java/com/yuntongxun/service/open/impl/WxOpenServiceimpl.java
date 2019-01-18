package com.yuntongxun.service.open.impl;

import com.yuntongxun.dao.open.OpenConfigDao;
import com.yuntongxun.model.po.open.OpenConfig;
import com.yuntongxun.service.open.WxOpenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lu on 2019/1/10.
 */
@Service
public class WxOpenServiceimpl implements WxOpenService {


    @Autowired
    private OpenConfigDao openConfigDao;

    @Override
    public void add(OpenConfig openConfig) throws Exception {
        openConfigDao.add(openConfig);
    }

    @Override
    public void updateByAppId(OpenConfig openConfig) throws Exception {
        openConfigDao.updateByAppId(openConfig);
    }

    @Override
    public void delete(OpenConfig openConfig) throws Exception {
        openConfigDao.delete(openConfig);
    }

    @Override
    public OpenConfig get(OpenConfig openConfig) throws Exception {
        return openConfigDao.getOpenConfigList(openConfig) == null ? null : openConfigDao.getOpenConfigList(openConfig).get(0);
    }

    @Override
    public List<OpenConfig> getOpenConfigList(OpenConfig openConfig) throws Exception {
        return openConfigDao.getOpenConfigList(openConfig);
    }
}

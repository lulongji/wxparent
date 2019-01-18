package com.yuntongxun.service.wechat.impl;

import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.dao.wechat.WechatDao;
import com.yuntongxun.model.constants.WxRedisConstant;
import com.yuntongxun.model.po.wx.BaseConfig;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.service.message.WxMessageHandel;
import com.yuntongxun.service.wechat.WechatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 15:43 2018/11/12
 */
@Service
public class WechatServiceImpl implements WechatService {


    private static final Logger logger = LoggerFactory.getLogger(WechatServiceImpl.class);

    @Autowired
    private WechatDao wechatDao;

    @Autowired
    private WxMessageHandel wxMessageHandel;

    @Override
    public BaseConfig getWxBaseConfig(BaseConfig baseConfig) throws Exception {
        return wechatDao.getWxBaseConfig(baseConfig);
    }

    @Override
    public List<BaseConfig> getWxBaseConfigList(BaseConfig baseConfig) throws Exception {
        return wechatDao.getWxBaseConfigList(baseConfig);
    }


    @Override
    public String getToken(String appid) throws Exception {
        String keys = CommonUtils.getRedisConstant(WxRedisConstant.WX_TOKEN, appid);
        Object token = RedisTemplateUtil.get(keys);
        String tokenVal = null;
        if (null == token) {
            BaseConfig baseConfig = new BaseConfig();
            baseConfig.setAppid(appid);
            BaseConfig bc = wechatDao.getWxBaseConfig(baseConfig);
            if (bc != null) {
                tokenVal = bc.getToken();
                RedisTemplateUtil.set(keys, tokenVal, 0, TimeUnit.SECONDS);
            }
        } else {
            tokenVal = token.toString();
        }
        return tokenVal;
    }

    @Override
    public String getEncodingAESKey(String appid) throws Exception {
        String encodingAESKeyVal = null;
        String keys = CommonUtils.getRedisConstant(WxRedisConstant.WX_ENCODINGAES_KEY, appid);
        Object encodingAESKey = RedisTemplateUtil.get(keys);
        if (null == encodingAESKey) {
            BaseConfig baseConfig = new BaseConfig();
            baseConfig.setAppid(appid);
            BaseConfig bc = wechatDao.getWxBaseConfig(baseConfig);
            if (bc != null) {
                encodingAESKeyVal = bc.getEncodingasekey();
                RedisTemplateUtil.set(keys, encodingAESKeyVal, 0, TimeUnit.SECONDS);
            }
        } else {
            encodingAESKeyVal = encodingAESKey.toString();
        }
        return encodingAESKeyVal;
    }

    @Override
    public String getAppSecret(String appid) throws Exception {
        String appSecretVal = null;
        String keys = CommonUtils.getRedisConstant(WxRedisConstant.WX_APPSECRET, appid);
        Object appSecretKey = RedisTemplateUtil.get(keys);
        if (null == appSecretKey) {
            BaseConfig baseConfig = new BaseConfig();
            baseConfig.setAppid(appid);
            BaseConfig bc = wechatDao.getWxBaseConfig(baseConfig);
            if (bc != null) {
                appSecretVal = bc.getAppsecret();
                RedisTemplateUtil.set(keys, appSecretVal, 0, TimeUnit.SECONDS);
            }
        } else {
            appSecretVal = appSecretKey.toString();
        }
        return appSecretVal;
    }

    @Override
    public String getRongLianAppid(String appid) throws Exception {
        String rongLianAppidVal = null;
        String keys = CommonUtils.getRedisConstant(WxRedisConstant.WX_RONGLIANAPPID, appid);
        Object rongLianAppidKey = RedisTemplateUtil.get(keys);
        if (null == rongLianAppidKey) {
            BaseConfig baseConfig = new BaseConfig();
            baseConfig.setAppid(appid);
            BaseConfig bc = wechatDao.getWxBaseConfig(baseConfig);
            if (bc != null) {
                rongLianAppidVal = bc.getRonglianappid();
                RedisTemplateUtil.set(keys, rongLianAppidVal, 0, TimeUnit.SECONDS);
            }
        } else {
            rongLianAppidVal = rongLianAppidKey.toString();
        }
        return rongLianAppidVal;
    }


}

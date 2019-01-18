package com.yuntongxun.utils;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.wechat.util.CommonUtil;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.wechat.open.AuthorizerAccessToken;
import com.yuntongxun.model.wechat.open.ComponentToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class WxOpenUtil {

    private static Logger logger = LoggerFactory.getLogger(WxOpenUtil.class);

    /**
     * 获取component_access_token
     *
     * @param jsondata
     * @return
     */
    public static ComponentToken getComponentccessToken(String jsondata) {
        ComponentToken token = null;
        try {
            JSONObject jsonObject = CommonUtil.httpsRequest(WxConstant.COMPONENT_ACCESS_TOKEN_URI, "POST", jsondata);
            if (null != jsonObject) {
                token = new ComponentToken();
                token.setComponentAccessToken(jsonObject.getString("component_access_token"));
                token.setExpiresIn(jsonObject.getString("expires_in"));
            }
        } catch (Exception e) {
            logger.error("getComponentccessToken#exception:", e);
            e.printStackTrace();
        }
        return token;
    }


    /**
     * 获取微信端authorizerAccessToken 、authorizerRefreshToken
     *
     * @param jsondata
     * @return
     */
    public static AuthorizerAccessToken getAuthorizerAccessTokenToWx(String jsondata) {
        AuthorizerAccessToken token = null;
        try {
            JSONObject jsonObject = CommonUtil.httpsRequest(WxConstant.AUTHORIZER_ACCESS_TOKEN, "POST", jsondata);
            if (null != jsonObject) {
                token = new AuthorizerAccessToken();
                token.setAuthorizerAccessToken(jsonObject.getString("authorizer_access_token"));
                token.setExpiresIn(jsonObject.getString("expires_in"));
                token.setAuthorizerRefreshToken(jsonObject.getString("authorizer_refresh_token"));
            } else {
                throw new Exception("Get authorizer_access_token error: " + jsonObject);
            }
        } catch (Exception e) {
            logger.error("getAuthorizerAccessTokenTo#exception:", e);
        }
        return token;
    }


}


package com.yuntongxun.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.base.wechat.common.WxConsts;
import com.yuntongxun.base.wechat.util.CommonUtil;
import com.yuntongxun.base.wechat.util.HttpClientUtils;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.constants.WxRedisConstant;
import com.yuntongxun.model.wechat.message.TextMessage;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.model.wechat.wx.Token;
import com.yuntongxun.model.wechat.wx.UserInfo;
import com.yuntongxun.model.wechat.wx.WeChatOauth2Token;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by lu on 2017/12/13. 类名: WeChatUtil </br>
 * 描述: 微信通用工具类 </br>
 * 发布版本：V1.0 </br>
 */
public class WeChatUtil {

    private static Logger log = LoggerFactory.getLogger(WeChatUtil.class);

    /**
     * 获取accessToken
     *
     * @param APPID
     * @return
     */
    public static String getAccessToken(String APPID) {
        String accessToken = null;
        try {
            Object o = RedisTemplateUtil.get(CommonUtils.getRedisConstant(WxRedisConstant.WX_ACCESSTOKEN, APPID));
            if (null == o) {
                accessToken = getToken(APPID).getAccessToken();
                RedisTemplateUtil.set(CommonUtils.getRedisConstant(WxRedisConstant.WX_ACCESSTOKEN, APPID), accessToken, 70, TimeUnit.MINUTES);
            } else {
                accessToken = o.toString();
            }
        } catch (Exception e) {
            log.error("Gets an exception to accessToken!", e);
            e.printStackTrace();
        }
        return accessToken;
    }

    /**
     * 获取接口访问凭证
     *
     * @param APPID
     * @return
     */
    public static Token getToken(String APPID) {
        String APPSECRET = null;
        try {
            APPSECRET = RedisTemplateUtil.get(CommonUtils.getRedisConstant(WxRedisConstant.WX_APPSECRET, APPID)).toString();
        } catch (Exception e) {
            log.error("Get password exception：", e);
        }
        Token token = null;
        String requestUrl = WxConstant.TOKEN_URL.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (JSONException e) {
                token = null;
                log.error("Gets token failed, errcode:{} errmsg:{}", jsonObject.getInteger("errcode"),
                        jsonObject.getString("errmsg"));
            }
        }
        return token;
    }


    /**
     * 获取用户信息
     *
     * @param accessToken
     * @param openId
     * @return
     */
    public static UserInfo getUserInfo(String accessToken, String openId) {
        UserInfo userInfo = null;
        String requestUrl = WxConstant.USER_URL_2.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        try {
            userInfo = new UserInfo();
            // 用户的标识
            userInfo.setOpenid(jsonObject.getString("openid"));
            // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
            userInfo.setSubscribe(jsonObject.getInteger("subscribe"));
            // 用户关注时间
            userInfo.setSubscribe_time(jsonObject.getString("subscribe_time"));
            // 昵称
            userInfo.setNickname(jsonObject.getString("nickname"));
            // 用户的性别（1是男性，2是女性，0是未知）
            userInfo.setSex(jsonObject.getInteger("sex"));
            // 用户所在国家
            userInfo.setCountry(jsonObject.getString("country"));
            // 用户所在省份
            userInfo.setProvince(jsonObject.getString("province"));
            // 用户所在城市
            userInfo.setCity(jsonObject.getString("city"));
            // 用户的语言，简体中文为zh_CN
            userInfo.setLanguage(jsonObject.getString("language"));
            // 用户头像
            userInfo.setHeadimgurl(jsonObject.getString("headimgurl"));
            //组
            userInfo.setGroupid(jsonObject.getInteger("groupid"));
        } catch (Exception e) {
            if (0 == userInfo.getSubscribe()) {
                log.error("User {} has unfollowed.", userInfo.getOpenid(), e);
            } else {
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("Failed to get user information, errcode:{" + errorCode + "} errmsg:{" + errorMsg + "}");
            }
        }
        return userInfo;
    }


    /**
     * 获取微信授权信息
     *
     * @param appId
     * @param appSecret
     * @param code
     * @return
     */
    public static WeChatOauth2Token getOauth2AccessToken(String appId, String appSecret, String code) {
        WeChatOauth2Token wat = null;
        String requestUrl = WxConstant.AUTH_URL.replace("APPID", appId).replace("APPSECRET", appSecret).replace("CODE", code);
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                wat = new WeChatOauth2Token();
                wat.setAccessToken(jsonObject.getString("access_token"));
                wat.setExpiresIn(jsonObject.getInteger("expires_in"));
                wat.setRefreshToken(jsonObject.getString("refresh_token"));
                wat.setOpenId(jsonObject.getString("openid"));
                wat.setScope(jsonObject.getString("scope"));
            } catch (Exception e) {
                wat = null;
                String errorCode = jsonObject.getString("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("Failed to obtain web page authorization certificate, errcode{},errMsg", errorCode, errorMsg);
            }
        }
        return wat;
    }


    /**
     * 网页授权通过code 获取用户信息
     *
     * @param code
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     */
    public static UserInfo oauth(String code, String appid, String secret) throws Exception {
        WeChatOauth2Token wt = getOauth2AccessToken(appid, secret, code);
        if (null == wt) {
            return getUserInfo(wt.getAccessToken(), wt.getOpenId());
        } else {
            return null;
        }
    }

    /**
     * 获取openId
     *
     * @param code
     * @param appid
     * @param secret
     * @return
     * @throws Exception
     */
    public static String getOpenId(String code, String appid, String secret) throws Exception {
        return getOauth2AccessToken(appid, secret, code).getOpenId();
    }


    /**
     * 创建菜单
     *
     * @param jsonMenu
     * @return
     */
    public static JSONObject createMenu(String jsonMenu, String APPID) {
        return CommonUtils.httpJson(WxConstant.MENU_URL.replace("ACCESS_TOKEN", WeChatUtil.getAccessToken(APPID)), jsonMenu);
    }

    /**
     * 查询菜单
     *
     * @param APPID
     * @return
     */
    public static JSONObject getMenu(String APPID) {
        return CommonUtil.httpsRequest(WxConstant.GET_MENU_URL.replace("ACCESS_TOKEN", WeChatUtil.getAccessToken(APPID)), "GET", null);

    }


    /**
     * 获取xml格式的Text类型的message消息
     *
     * @param FromUserName
     * @param ToUserName
     * @param respContent
     * @return
     */
    public static String getTextMessage(String FromUserName, String ToUserName, String respContent) throws Exception {
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(FromUserName);
        textMessage.setFromUserName(ToUserName);
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType(WxConsts.XmlMsgType.TEXT);
        textMessage.setContent(respContent);
        return CommonUtils.textMessageToXml(textMessage);
    }


    /**
     * 回复微信消息（客服接口）
     *
     * @param access_token
     * @param msg
     * @return
     */
    public static String replyMessage(String access_token, String msg) {
        if (StringUtils.isBlank(access_token)) {
            return null;
        }
        if (StringUtils.isNotBlank(access_token)) {
            String url = WxConstant.SEND_KF_MSG_URL.replace("[ACCESS_TOKEN]", access_token);
            String result = HttpClientUtils.post(url, msg);
            if (result != null) {
                return result;
            } else {
                log.info("The reply message interface failed and the request result was empty.");
            }
        } else {
            log.info("The reply message interface fails and the access_token is empty.");
        }
        return null;
    }


}


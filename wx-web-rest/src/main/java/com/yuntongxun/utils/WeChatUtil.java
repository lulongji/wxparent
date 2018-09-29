package com.yuntongxun.utils;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import com.yuntongxun.base.webchat.CommonUtil;
import com.yuntongxun.model.wechat.domain.RedisKeyConstants;
import com.yuntongxun.model.wechat.wx.Token;
import com.yuntongxun.model.wechat.wx.UserInfo;
import com.yuntongxun.model.wechat.wx.WeChatOauth2Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.TimeUnit;

/**
 * Created by lu on 2017/12/13. 类名: WeChatUtil </br>
 * 描述: 微信通用工具类 </br>
 * 发布版本：V1.0 </br>
 */
public class WeChatUtil {

    private static Logger log = LoggerFactory.getLogger(WeChatUtil.class);

    // 凭证获取（GET）
    public final static String token_url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";

    /**
     * 获取accessToken
     *
     * @return
     * @throws Exception
     */
    public static String getAccessToken(String APPID) {
        Object accessToken = null;
        try {
            accessToken = RedisTemplateUtil.get(RedisKeyConstants.WECHAT_ACCESS_TOKEN + APPID);
            if (null == accessToken) {
                accessToken = getToken(APPID).getAccessToken();
                RedisTemplateUtil.set(RedisKeyConstants.WECHAT_ACCESS_TOKEN + APPID, accessToken, 70, TimeUnit.MINUTES);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accessToken.toString();
    }

    /**
     * 获取接口访问凭证
     *
     * @return
     */
    public static Token getToken(String APPID) {

        //TODO 根据appid 查询APPSECRET
        String APPSECRET = "";

        Token token = null;
        String requestUrl = token_url.replace("APPID", APPID).replace("APPSECRET", APPSECRET);
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        if (null != jsonObject) {
            try {
                token = new Token();
                token.setAccessToken(jsonObject.getString("access_token"));
                token.setExpiresIn(jsonObject.getInteger("expires_in"));
            } catch (JSONException e) {
                token = null;
                log.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInteger("errcode"),
                        jsonObject.getString("errmsg"));
            }
        }
        return token;
    }

    /**
     * 获取用户信息
     *
     * @param accessToken 接口访问凭证
     * @param openId      用户标识
     * @return UserInfo
     */
    public static JSONObject getWechatUser(String accessToken, String openId) {
        // 拼接请求地址
        String requestUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
        requestUrl = requestUrl.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openId);
        // 获取用户信息
        JSONObject jsonObject = CommonUtil.httpsRequest(requestUrl, "GET", null);
        return jsonObject;
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
        JSONObject jsonObject = null;
        try {
            jsonObject = getWechatUser(accessToken, openId);
            userInfo = new UserInfo();
            // 用户的标识
            userInfo.setOpenId(jsonObject.getString("openid"));
            // 关注状态（1是关注，0是未关注），未关注时获取不到其余信息
            //userInfo.setSubscribe(jsonObject.getInteger("subscribe"));
            // 用户关注时间
            //userInfo.setSubscribeTime(jsonObject.getString("subscribe_time"));
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
            userInfo.setHeadImgUrl(jsonObject.getString("headimgurl"));
        } catch (Exception e) {
            e.printStackTrace();
            if (0 == userInfo.getSubscribe()) {
                log.error("用户{}已取消关注", userInfo.getOpenId());
            } else {
                int errorCode = jsonObject.getInteger("errcode");
                String errorMsg = jsonObject.getString("errmsg");
                log.error("获取用户信息失败 errcode:{" + errorCode + "} errmsg:{" + errorMsg + "}");
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
        WeChatOauth2Token wat = new WeChatOauth2Token();
        String requestUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + appId + "&secret=" + appSecret
                + "&code=" + code + "&grant_type=authorization_code";
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
                log.error("获取网页授权凭证失败 errcode{},errMsg", errorCode, errorMsg);
            }

        }
        return wat;
    }

    /**
     * 将请求参数转换为xml格式的string
     *
     * @param parameters 请求参数
     * @return
     * @Description：将请求参数转换为xml格式的string
     */
    @SuppressWarnings("rawtypes")
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 返回给微信的参数
     *
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     * @Description：返回给微信的参数
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    /**
     * url编码
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
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
        log.info("用户同意授权后的code: " + code);
        WeChatOauth2Token wt = getOauth2AccessToken(appid, secret, code);
        return getUserInfo(wt.getAccessToken(), wt.getOpenId());
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
        log.info("用户同意授权后的code: " + code);
        return getOauth2AccessToken(appid, secret, code).getOpenId();
    }


    /**
     * 生成用于获取access_token的Code的Url
     *
     * @param redirectUrl
     * @return
     */
    public static String getRequestCodeUrl(String APPID, String redirectUrl, String scope, String state) {
        return String.format("https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect",
                APPID, redirectUrl, scope, state);
    }


    /**
     * 创建菜单
     *
     * @param jsonMenu
     * @return
     */
    public static int createMenu(String jsonMenu, String APPID) {
        int status = 0;
        String accessoken = WeChatUtil.getAccessToken(APPID);
        String path = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=" + accessoken;
        try {
            URL url = new URL(path);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(jsonMenu.getBytes("UTF-8"));
            os.close();

            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] bt = new byte[size];
            is.read(bt);
            String message = new String(bt, "UTF-8");
            JSONObject jsonMsg = JSONObject.parseObject(message);
            status = Integer.parseInt(jsonMsg.getString("errcode"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return status;
    }

}


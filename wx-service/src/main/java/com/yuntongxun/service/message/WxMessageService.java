package com.yuntongxun.service.message;

import com.yuntongxun.base.wechat.WxType;
import com.yuntongxun.base.wechat.common.WxConsts;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.po.wx.WxUserInfo;
import com.yuntongxun.model.properties.AppFilesProperties;
import com.yuntongxun.model.wechat.utils.FileUtil;
import com.yuntongxun.model.wechat.wx.UserInfo;
import com.yuntongxun.service.open.OpenService;
import com.yuntongxun.service.wechat.WxUserInfoService;
import com.yuntongxun.utils.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Description: 处理微信消息
 * @Author: lulongji
 * @Date: Created in 19:48 2018/11/26
 */
@Service
public class WxMessageService {

    private static final Logger logger = LoggerFactory.getLogger(WxMessageService.class);

    @Autowired
    private WxMessageHandel wxMessageHandel;
    @Autowired
    private WxUserInfoService wxUserInfoService;
    @Autowired
    private AppFilesProperties appFilesProperties;
    @Autowired
    private OpenService openService;


    /**
     * 微信公众平台
     *
     * @param map
     * @return
     * @throws Exception
     */
    public void doMessage(Map<String, String> map) throws Exception {

        // 公众帐号
        String ToUserName = map.get("ToUserName");

        // 发送方帐号（open_id）
        String FromUserName = map.get("FromUserName");
        String CreateTime = map.get("CreateTime");

        // 消息类型
        String MsgType = map.get("MsgType");
        String Event = map.get("Event");

        //微信公众号appid
        String AppId = map.get("appid");
        String wxType = map.get("WxType");

        String ronglianappid = map.get("ronglianappid");

        logger.info("doMessage==========》:AppId：" + AppId +
                ",fromUserName:" + FromUserName +
                ",toUserName:" + ToUserName +
                ",msgType:" + MsgType +
                ",Event:" + Event +
                ",CreateTime:" + CreateTime);

        if (MsgType != null) {
            String access_token = null;
            if (WxType.MP.toString().equals(wxType)) {
                access_token = WeChatUtil.getAccessToken(AppId);
            }
            if (WxType.Open.toString().equals(wxType)) {
                access_token = openService.getAuthorizerAccessToken(AppId);
            }
            if (WxConsts.XmlMsgType.EVENT.equalsIgnoreCase(MsgType)) {
                if (WxConsts.EventType.SUBSCRIBE.equalsIgnoreCase(Event)) {
                    UserInfo userInfo = WeChatUtil.getUserInfo(access_token, FromUserName);
                    WxUserInfo wxUserInfo = new WxUserInfo();
                    wxUserInfo.setAppid(AppId);
                    wxUserInfo.setNickname(userInfo.getNickname());
                    wxUserInfo.setSex(userInfo.getSex());
                    wxUserInfo.setHeadimg(userInfo.getHeadimgurl());
                    wxUserInfo.setCity(userInfo.getCity());
                    wxUserInfo.setProvince(userInfo.getProvince());
                    wxUserInfo.setCountry(userInfo.getCountry());
                    wxUserInfo.setOpenid(FromUserName);
                    wxUserInfo.setGroupid(userInfo.getGroupid());
                    wxUserInfoService.addWxUserInfo(wxUserInfo);
                }
            } else {
                if (WxConsts.XmlMsgType.VIDEO.equalsIgnoreCase(MsgType) ||
                        WxConsts.XmlMsgType.SHORTVIDEO.equalsIgnoreCase(MsgType) ||
                        WxConsts.XmlMsgType.VOICE.equalsIgnoreCase(MsgType) ||
                        WxConsts.XmlMsgType.IMAGE.equalsIgnoreCase(MsgType)) {

                    String fileUrl = FileUtil.uploadFile(WxConstant.MEDIA_FILE_URL.
                            replace("[ACCESS_TOKEN]", access_token).
                            replace("[MEDIA_ID]", map.get("MediaId")), MsgType, ronglianappid, FromUserName, appFilesProperties);
                    map.put("url", fileUrl);
                }
            }
        }
        this.onMessage(map);
    }

    /**
     * @param map
     * @throws Exception
     */
    public void onMessage(Map<String, String> map) throws Exception {
        WxMessageTask messageTask = new WxMessageTask(map);
        messageTask.setWxMessageHandel(wxMessageHandel);
        WxMessageFixedThreadPool.addTask(messageTask);
    }


}

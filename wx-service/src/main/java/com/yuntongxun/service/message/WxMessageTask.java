package com.yuntongxun.service.message;

import com.yuntongxun.base.wechat.common.WxConsts;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 11:43 2018/11/17
 */
public class WxMessageTask implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(WxMessageTask.class);

    private Map<String, String> notifyMessageMap;

    @Autowired
    private WxMessageHandel wxMessageHandel;


    public WxMessageTask(Map<String, String> notifyMessageMap) {
        this.notifyMessageMap = notifyMessageMap;
    }

    @Override
    public void run() {
        try {
            String sendMcmMsgUrl = notifyMessageMap.get("sendMcmMsgUrl");
            wxMessageHandel.sendMsg(getFormatMsgData(notifyMessageMap), sendMcmMsgUrl);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    public String getFormatMsgData(Map<String, String> notifyMessageMap) {
        String msgType = notifyMessageMap.get("MsgType");
        JSONObject jsonData = new JSONObject();
        //TODO 5在mcm中为微信消息类型，重构mcm时抽出  1，标识event事件如分配坐席  3，为发送消息
        jsonData.element("MCMEvent", 5);

        jsonData.element("openID", notifyMessageMap.get("appid"));
        jsonData.element("userID", notifyMessageMap.get("FromUserName"));
        jsonData.element("createTime", notifyMessageMap.get("CreateTime"));
        jsonData.element("appId", notifyMessageMap.get("ronglianappid"));
        jsonData.element("msgId", notifyMessageMap.get("MsgId"));

        //TODO 查询微信用户基础信息缓存redis
        jsonData.element("nickName", notifyMessageMap.get(""));
        jsonData.element("headImg", notifyMessageMap.get(""));
        jsonData.element("msgType", msgType);

        if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.EVENT)) {
            String event = notifyMessageMap.get("Event");
            if (event.equalsIgnoreCase(WxConsts.EventType.SUBSCRIBE)) {
                jsonData.element("eventKey", notifyMessageMap.get("EventKey"));
                jsonData.element("ticket", notifyMessageMap.get("Ticket"));
            }
            if (WxConsts.EventType.SCAN.equalsIgnoreCase(event)) {
                jsonData.element("eventKey", notifyMessageMap.get("EventKey"));
                jsonData.element("ticket", notifyMessageMap.get("Ticket"));
            }
            if (WxConsts.EventType.CLICK.equalsIgnoreCase(event) ||
                    WxConsts.EventType.VIEW.equals(event)) {
                jsonData.element("MCMEvent", notifyMessageMap.get("EventKey"));
                jsonData.element("eventKey", notifyMessageMap.get("EventKey"));
            }
            if (WxConsts.EventType.LOCATION.equalsIgnoreCase(event)) {
                jsonData.element("latitude", notifyMessageMap.get("Latitude"));
                jsonData.element("longitude", notifyMessageMap.get("Longitude"));
                jsonData.element("precision", notifyMessageMap.get("Precision"));
            }
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.TEXT)) {
            jsonData.element("content", notifyMessageMap.get("Content"));
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.SHORTVIDEO) ||
                msgType.equalsIgnoreCase(WxConsts.XmlMsgType.VIDEO)) {
            jsonData.element("thumbMediaId", notifyMessageMap.get("ThumbMediaId"));
            jsonData.element("url", notifyMessageMap.get("url"));
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.VOICE)) {
            jsonData.element("format", notifyMessageMap.get("Format"));
            jsonData.element("url", notifyMessageMap.get("url"));
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.IMAGE)) {
            jsonData.element("url", notifyMessageMap.get("url"));
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.LINK)) {
            jsonData.element("title", notifyMessageMap.get("Title"));
            jsonData.element("description", notifyMessageMap.get("Description"));
            jsonData.element("url", notifyMessageMap.get("Url"));
            jsonData.element("label", notifyMessageMap.get("Label"));
        } else if (msgType.equalsIgnoreCase(WxConsts.XmlMsgType.LOCATION)) {
            jsonData.element("location_X", notifyMessageMap.get("Location_X"));
            jsonData.element("location_Y", notifyMessageMap.get("Location_Y"));
            jsonData.element("scale", notifyMessageMap.get("Scale"));
            jsonData.element("label", notifyMessageMap.get("Label"));
            jsonData.element("content", "位置信息：" + notifyMessageMap.get("Label"));
        } else {
            logger.info("Sorry, this message type is not supported for the time being.--->" + msgType);
        }
        return jsonData.toString();
    }

    public void setWxMessageHandel(WxMessageHandel wxMessageHandel) {
        this.wxMessageHandel = wxMessageHandel;
    }
}

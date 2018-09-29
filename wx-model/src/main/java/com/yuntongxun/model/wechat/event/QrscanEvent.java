package com.yuntongxun.model.wechat.event;

/**
 * 扫描带参数二维码事件 用户扫描带场景值二维码时，可能推送以下两种事件： 用户已关注时的事件推送
 *
 * @author apple
 */
public class QrscanEvent extends BaseEvent {

    private String ToUserName; // 开发者微信号
    private String FromUserName;// 发送方帐号（一个OpenID）
    private String CreateTime; // 消息创建时间 （整型）
    private String MsgType; // 消息类型 event
    private String Event; // 事件类型
    private String EventKey; // 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
    private String Ticket; // 二维码的ticket，可用来换取二维码图片

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public String getFromUserName() {
        return FromUserName;
    }

    public void setFromUserName(String fromUserName) {
        FromUserName = fromUserName;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getMsgType() {
        return MsgType;
    }

    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getEvent() {
        return Event;
    }

    public void setEvent(String event) {
        Event = event;
    }

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }

    public String getTicket() {
        return Ticket;
    }

    public void setTicket(String ticket) {
        Ticket = ticket;
    }

}

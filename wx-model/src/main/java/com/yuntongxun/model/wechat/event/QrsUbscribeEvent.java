package com.yuntongxun.model.wechat.event;

/**
 * 扫描带参数二维码事件 用户扫描带场景值二维码时，可能推送以下两种事件： 用户未关注时，进行关注后的事件推送
 *
 * @author apple
 */
public class QrsUbscribeEvent extends BaseEvent {

    private String EventKey; // 事件KEY值，qrscene_为前缀，后面为二维码的参数值
    private String Ticket; // 二维码的ticket，可用来换取二维码图片

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

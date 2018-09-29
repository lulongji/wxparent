package com.yuntongxun.model.wechat.event;

/**
 * 自定义菜单事件
 * <p>
 * 点击菜单跳转链接时的事件推送
 *
 * @author apple
 */
public class MenuViewEvent extends BaseEvent {

    private String EventKey; // 事件KEY值，设置的跳转URL

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}

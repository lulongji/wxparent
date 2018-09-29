package com.yuntongxun.model.wechat.event;

/**
 * 自定义菜单事件
 * 点击菜单拉取消息时的事件推送
 *
 * @author apple
 */
public class MenuClickEvent extends BaseEvent {

    private String EventKey; // 事件KEY值，与自定义菜单接口中KEY值对应

    public String getEventKey() {
        return EventKey;
    }

    public void setEventKey(String eventKey) {
        EventKey = eventKey;
    }
}

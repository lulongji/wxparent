package com.yuntongxun.model.wechat.event;

import lombok.Data;

/**
 * 自定义菜单事件
 * <p>
 * 点击菜单跳转链接时的事件推送
 *
 * @author apple
 */
@Data
public class MenuViewEvent extends BaseEvent {

    private String EventKey; // 事件KEY值，设置的跳转URL
}

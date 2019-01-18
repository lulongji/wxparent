package com.yuntongxun.model.wechat.event;

import lombok.Data;

/**
 * 自定义菜单事件
 * 点击菜单拉取消息时的事件推送
 *
 * @author apple
 */
@Data
public class MenuClickEvent extends BaseEvent {

    private String EventKey; // 事件KEY值，与自定义菜单接口中KEY值对应
}

package com.yuntongxun.model.wechat.event;

import lombok.Data;

/**
 * @Description:
 * @Author: lulongji
 * @Date: Created in 14:10 2018/9/29
 */
@Data
public class BaseEvent {
    /**
     * 开发者微信号
     */
    private String ToUserName;
    /**
     * 发送方帐号（一个OpenID）
     */
    private String FromUserName;
    /**
     * 消息创建时间 （整型）
     */
    private long CreateTime;
    /**
     * 消息类型 event
     */
    private String MsgType;
    /**
     *
     */
    private String Event;

}

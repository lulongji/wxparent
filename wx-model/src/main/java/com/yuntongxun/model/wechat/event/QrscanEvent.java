package com.yuntongxun.model.wechat.event;

import lombok.Data;

/**
 * 扫描带参数二维码事件 用户扫描带场景值二维码时，可能推送以下两种事件： 用户已关注时的事件推送
 *
 * @author apple
 */
@Data
public class QrscanEvent extends BaseEvent {

    private String ToUserName; // 开发者微信号
    private String FromUserName;// 发送方帐号（一个OpenID）
    private String MsgType; // 消息类型 event
    private String Event; // 事件类型
    private String EventKey; // 事件KEY值，是一个32位无符号整数，即创建二维码时的二维码scene_id
    private String Ticket; // 二维码的ticket，可用来换取二维码图片

}

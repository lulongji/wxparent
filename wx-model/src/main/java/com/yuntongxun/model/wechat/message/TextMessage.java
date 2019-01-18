package com.yuntongxun.model.wechat.message;

import lombok.Data;

/**
 * 文本消息
 *
 * @author lu
 */
@Data
public class TextMessage extends BaseMessage {
    // 回复的消息内容
    private String Content;
}
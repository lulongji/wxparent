package com.yuntongxun.model.wechat.message;

import lombok.Data;

/**
 * 链接消息
 *
 * @author lu
 */
@Data
public class LinkMessage extends BaseMessage {
    private String Title; // 消息标题
    private String Description; // 消息描述
    private String Url; // 消息链接

}

package com.yuntongxun.model.wechat.wx;

import lombok.Data;

/**
 * Created by lu on 2017/2/15.
 * <p>
 * description:微信消息实体
 */

@Data
public class WechatMessage {
    private String toUserName;
    private String fromUserName;
    private String createTime;
    private String msgType;
    private String msgID;
}

package com.yuntongxun.model.wechat.message;

import lombok.Data;

/**
 * 地理位置消息
 *
 * @author lu
 */
@Data
public class LocationMessage extends BaseMessage {
    private String MsgType;// location
    private String Location_X;// 地理位置维度
    private String Location_Y;// 地理位置经度
    private String Scale;// 地图缩放大小
    private String Label;// 地理位置信息
}

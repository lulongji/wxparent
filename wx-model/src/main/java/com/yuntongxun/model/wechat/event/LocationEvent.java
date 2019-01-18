package com.yuntongxun.model.wechat.event;


import lombok.Data;

/**
 * 上报地理位置事件模型
 *
 * @author apple
 */
@Data
public class LocationEvent extends BaseEvent {

    private String Latitude; // 地理位置纬度
    private String Longitude; // 地理位置经度
    private String Precision; // 地理位置精度
}

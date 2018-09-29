package com.yuntongxun.model.wechat.message;

/**
 * 地理位置消息
 *
 * @author lu
 */
public class LocationMessage extends BaseMessage {
    private String MsgType;// location
    private String Location_X;// 地理位置维度
    private String Location_Y;// 地理位置经度
    private String Scale;// 地图缩放大小
    private String Label;// 地理位置信息

    @Override
    public String getMsgType() {
        return MsgType;
    }

    @Override
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getLocation_X() {
        return Location_X;
    }

    public void setLocation_X(String location_X) {
        Location_X = location_X;
    }

    public String getLocation_Y() {
        return Location_Y;
    }

    public void setLocation_Y(String location_Y) {
        Location_Y = location_Y;
    }

    public String getScale() {
        return Scale;
    }

    public void setScale(String scale) {
        Scale = scale;
    }

    public String getLabel() {
        return Label;
    }

    public void setLabel(String label) {
        Label = label;
    }
}

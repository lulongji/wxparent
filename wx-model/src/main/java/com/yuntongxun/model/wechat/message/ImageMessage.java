package com.yuntongxun.model.wechat.message;

/**
 * 图片消息
 *
 * @author apple
 */
public class ImageMessage extends BaseMessage {

    private String MsgType; // image
    private String PicUrl; // 图片链接
    private String MediaId; // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

    @Override
    public String getMsgType() {
        return MsgType;
    }

    @Override
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }
}

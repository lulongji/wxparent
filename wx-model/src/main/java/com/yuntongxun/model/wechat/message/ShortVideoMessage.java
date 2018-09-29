package com.yuntongxun.model.wechat.message;

/**
 * 小视频消息
 *
 * @author lu
 */
public class ShortVideoMessage extends BaseMessage {
    private String MsgType; // 小视频为shortvideo
    private String MediaId; // 视频消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String ThumbMediaId; // 视频消息缩略图的媒体id，可以调用多媒体文件下载接口拉取数据。

    @Override
    public String getMsgType() {
        return MsgType;
    }

    @Override
    public void setMsgType(String msgType) {
        MsgType = msgType;
    }

    public String getMediaId() {
        return MediaId;
    }

    public void setMediaId(String mediaId) {
        MediaId = mediaId;
    }

    public String getThumbMediaId() {
        return ThumbMediaId;
    }

    public void setThumbMediaId(String thumbMediaId) {
        ThumbMediaId = thumbMediaId;
    }
}

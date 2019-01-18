package com.yuntongxun.model.wechat.message;

import lombok.Data;

/**
 * 图片消息
 *
 * @author apple
 */
@Data
public class ImageMessage extends BaseMessage {

    private String MsgType; // image
    private String PicUrl; // 图片链接
    private String MediaId; // 图片消息媒体id，可以调用多媒体文件下载接口拉取数据。

}

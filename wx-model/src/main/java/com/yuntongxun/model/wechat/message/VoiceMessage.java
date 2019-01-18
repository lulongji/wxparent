package com.yuntongxun.model.wechat.message;

import lombok.Data;

/**
 * 语音消息
 *
 * @author lu
 */
@Data
public class VoiceMessage extends BaseMessage {
    private String MsgType; // 语音为voice
    private String MediaId; // 语音消息媒体id，可以调用多媒体文件下载接口拉取数据。
    private String Format; // 语音格式，如amr，speex等
    private String Recognition;// Recognition为语音识别结果，使用UTF8编码

}

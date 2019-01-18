package com.yuntongxun.model.wechat.message;

import com.yuntongxun.model.wechat.wx.Music;
import lombok.Data;

/**
 * 音乐消息
 *
 * @author lu
 */
@Data
public class MusicMessage extends BaseMessage {
    private Music music;
}
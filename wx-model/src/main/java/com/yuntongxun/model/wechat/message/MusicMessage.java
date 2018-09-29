package com.yuntongxun.model.wechat.message;

import com.yuntongxun.model.wechat.wx.Music;

/**
 * 音乐消息
 *
 * @author lu
 */
public class MusicMessage extends BaseMessage {
    // 音乐
    private com.yuntongxun.model.wechat.wx.Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
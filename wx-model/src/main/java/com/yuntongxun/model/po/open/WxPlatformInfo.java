package com.yuntongxun.model.po.open;

import lombok.Data;

/**
 * Created by lu on 2018/12/20.
 */

@Data
public class WxPlatformInfo {

    private Long id;
    private String appid;
    private String nickName;
    private String headImg;
    private String alias;
    private String userName;
    private String ronglianappid;
    private String createtime;

    @Override
    public String toString() {
        return "WxPlatformInfo{" +
                "id=" + id +
                ", appid='" + appid + '\'' +
                ", nickName='" + nickName + '\'' +
                ", headImg='" + headImg + '\'' +
                ", alias='" + alias + '\'' +
                ", userName='" + userName + '\'' +
                ", ronglianappid='" + ronglianappid + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}

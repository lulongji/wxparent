package com.yuntongxun.model.po.wx;

import lombok.Data;

import java.sql.Date;

/**
 * @Description: 微信用户基础信息
 * @Author: lu
 * @Date: Created in 19:17 2018/11/26
 */
@Data
public class WxUserInfo {
    private String appid;
    private String nickname;
    private Integer sex;
    private String headimg;
    private String city;
    private String province;
    private String country;
    private String openid;
    private Integer groupid;
    private Date createtime;
    private Date updatetime;

    @Override
    public String toString() {
        return "WxUserInfo{" +
                ", appid='" + appid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", sex=" + sex +
                ", headimg='" + headimg + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", country='" + country + '\'' +
                ", openid='" + openid + '\'' +
                ", groupid='" + groupid + '\'' +
                ", createtime=" + createtime +
                ", updatetime=" + updatetime +
                '}';
    }
}

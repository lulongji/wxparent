package com.yuntongxun.model.po.wx;


import lombok.Data;

/**
 * @Description: 微信公众平台基础配置
 * @Author: lu
 * @Date: Created in 16:20 2018/11/12
 */

@Data
public class BaseConfig {

    private Long id;
    private String ronglianappid;
    private String appid;
    private String appsecret;
    private String token;
    private String isase;
    private String encodingasekey;
    private String companyname;
    private String createtime;

    @Override
    public String toString() {
        return "BaseConfig{" +
                "id=" + id +
                ", ronglianappid='" + ronglianappid + '\'' +
                ", appid='" + appid + '\'' +
                ", appsecret='" + appsecret + '\'' +
                ", token='" + token + '\'' +
                ", isase='" + isase + '\'' +
                ", encodingasekey='" + encodingasekey + '\'' +
                ", companyname='" + companyname + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}

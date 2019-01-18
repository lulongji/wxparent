package com.yuntongxun.model.po.open;

import lombok.Data;

import java.util.Date;

/**
 * @Description: 第三方平台基础配置
 * @Author: lu
 * @Date: Created in 16:35 2018/11/19
 */

@Data
public class OpenConfig {

    private Long id;
    private String appid;
    private String accessId;
    private String authorizerAccessToken;
    private String authorizerAccessTokenExpiresIn;
    private String authorizerAccessTokenTime;
    private String authorizerRefreshToken;
    private String authCode;
    private String authCodeExpiresIn;
    private String authCodeTime;
    private String funcInfo;
    private Date createtime;


    @Override
    public String toString() {
        return "OpenConfig{" +
                "id=" + id +
                ", appid='" + appid + '\'' +
                ", accessId='" + accessId + '\'' +
                ", authorizerAccessToken='" + authorizerAccessToken + '\'' +
                ", authorizerAccessTokenExpiresIn='" + authorizerAccessTokenExpiresIn + '\'' +
                ", authorizerAccessTokenTime='" + authorizerAccessTokenTime + '\'' +
                ", authorizerRefreshToken='" + authorizerRefreshToken + '\'' +
                ", authCode='" + authCode + '\'' +
                ", authCodeExpiresIn='" + authCodeExpiresIn + '\'' +
                ", authCodeTime='" + authCodeTime + '\'' +
                ", funcInfo='" + funcInfo + '\'' +
                ", createtime='" + createtime + '\'' +
                '}';
    }
}

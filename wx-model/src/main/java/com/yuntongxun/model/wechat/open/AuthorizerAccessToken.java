package com.yuntongxun.model.wechat.open;

import lombok.Data;

/**
 * Created by lu on 2019/1/2.
 */

@Data
public class AuthorizerAccessToken {

    private String authorizerAccessToken;
    private String expiresIn;
    private String authorizerRefreshToken;

    @Override
    public String toString() {
        return "AuthorizerAccessToken{" +
                "authorizerAccessToken='" + authorizerAccessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                ", authorizerRefreshToken='" + authorizerRefreshToken + '\'' +
                '}';
    }
}

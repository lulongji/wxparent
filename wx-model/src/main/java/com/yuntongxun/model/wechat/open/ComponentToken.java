package com.yuntongxun.model.wechat.open;

import lombok.Data;

/**
 * Created by lu on 2018/12/20.
 */

@Data
public class ComponentToken {

    private String componentAccessToken;
    private String expiresIn;

    @Override
    public String toString() {
        return "ComponentToken{" +
                "componentAccessToken='" + componentAccessToken + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }
}

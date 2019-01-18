package com.yuntongxun.model.wechat.open;

import lombok.Data;

/**
 * Created by lu on 2018/12/20.
 */

@Data
public class PreAuthCode {

    private String preAuthCode;
    private String expiresIn;


    @Override
    public String toString() {
        return "PreAuthCode{" +
                "preAuthCode='" + preAuthCode + '\'' +
                ", expiresIn='" + expiresIn + '\'' +
                '}';
    }
}

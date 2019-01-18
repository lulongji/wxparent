package com.yuntongxun.model.wechat.wx;

import lombok.Data;

/**
 * Created by lu on 2017/2/10.
 * <p>
 * Description:凭证
 */

@Data
public class Token {

    private String accessToken;// 接口访问凭证

    private int expiresIn; // 凭证有效期，单位：秒

}

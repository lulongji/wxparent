package com.yuntongxun.model.wechat.wx;

import lombok.Data;

/**
 * Created by lu on 2017/2/13.
 * <p>
 * Description:微信授权实体
 */
@Data
public class WeChatOauth2Token {

    private String accessToken;
    private Integer expiresIn;
    private String refreshToken;
    private String openId;
    private String scope;

}

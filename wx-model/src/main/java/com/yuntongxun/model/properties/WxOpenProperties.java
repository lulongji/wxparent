package com.yuntongxun.model.properties;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lu
 * @Description 开放平台基础配置
 */
@Data
@ConfigurationProperties(prefix = "wx.open")
public class WxOpenProperties {
    /**
     * 设置微信三方平台的appid
     */
    private String componentAppId;

    /**
     * 设置微信三方平台的app secret
     */
    private String componentSecret;

    /**
     * 设置微信三方平台的token
     */
    private String componentToken;

    /**
     * 设置微信三方平台的EncodingAESKey
     */
    private String componentAesKey;

    /**
     * 线程池分配数量
     */
    private int poolNum;


    /**
     * 地址
     */
    private String uri;

    /**
     * 向mcm发送消息
     */
    private String sendMcmMsgUrl;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
                ToStringStyle.MULTI_LINE_STYLE);
    }
}

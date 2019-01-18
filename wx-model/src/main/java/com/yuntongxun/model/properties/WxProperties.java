package com.yuntongxun.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author lu
 * @Description
 */
@Data
@ConfigurationProperties(prefix = "wx")
public class WxProperties {

    /**
     * 线程池分配数量
     */
    private int poolNum;

    /**
     * 向mcm发送消息
     */
    private String sendMcmMsgUrl;


    @Override
    public String toString() {
        return "WxProperties{" +
                "poolNum=" + poolNum +
                ", sendMcmMsgUrl='" + sendMcmMsgUrl + '\'' +
                '}';
    }
}

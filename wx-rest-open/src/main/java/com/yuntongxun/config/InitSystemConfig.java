package com.yuntongxun.config;

import com.yuntongxun.model.properties.AppFilesProperties;
import com.yuntongxun.model.properties.WxOpenProperties;
import com.yuntongxun.service.message.WxMessageFixedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @Description: 初始化系统
 * @Author: lulongji
 * @Date: Created in 15:53 2018/11/17
 */

@Component
@Order(0)
@EnableConfigurationProperties({WxOpenProperties.class, AppFilesProperties.class})
public class InitSystemConfig implements CommandLineRunner, EnvironmentAware {


    private static final Logger logger = LoggerFactory.getLogger(InitSystemConfig.class);

    @Autowired
    private WxOpenProperties wxOpenProperties;

    @Override
    public void run(String... strings) throws Exception {
        logger.info("Started system init params.");
        new WxMessageFixedThreadPool(wxOpenProperties.getPoolNum());
    }

    @Override
    public void setEnvironment(Environment environment) {

    }
}

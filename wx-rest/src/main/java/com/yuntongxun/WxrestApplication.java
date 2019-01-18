package com.yuntongxun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@SpringBootApplication
@MapperScan("com.yuntongxun.dao")
@ImportResource(locations = {"classpath:spring/spring-redis.xml"})
@EnableAutoConfiguration
public class WxrestApplication {

    public static void main(String[] args) {

        SpringApplication.run(WxrestApplication.class, args);

    }


}

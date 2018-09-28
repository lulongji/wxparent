package com.yuntongxun;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement // 开启事务管理
@SpringBootApplication
@MapperScan("com.yuntongxun.dao")
public class WxrestApplication {

    public static void main(String[] args) {

        SpringApplication.run(WxrestApplication.class, args);

    }


}

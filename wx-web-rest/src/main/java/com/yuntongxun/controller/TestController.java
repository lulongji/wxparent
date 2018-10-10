package com.yuntongxun.controller;


import com.yuntongxun.base.cache.redis.springTemplate.RedisTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Description: Hello World
 * @Author: lu
 */
@RestController
@EnableAutoConfiguration
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${test}")
    private String path;

    @RequestMapping("/")
    public String test() {
        return path;
    }


    @RequestMapping("/test1")
    public String test1(Map<String, Object> map) {
        map.put("nameKey111", path);
        return map.toString();
    }


    @RequestMapping("/redis")
    public String redis() throws Exception {
        redisTemplate.opsForValue().set("aaa", "ssssss");
        RedisTemplateUtil.set("111111", "wwwww", 1, TimeUnit.MINUTES);
        return "cddcdc";
    }


}

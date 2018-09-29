package com.yuntongxun.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: Hello World
 * @Author: lu
 */
@Controller
@EnableAutoConfiguration
public class TestController {

    @Autowired
    private RedisTemplate redisTemplate;


    @Value("${test}")
    private String path;

    @RequestMapping("/")
    @ResponseBody
    public String test() {
        return path;
    }


    @RequestMapping("/test1")
    public String test1(Map<String, Object> map) {
        map.put("nameKey", path);
        return "test";
    }

    @RequestMapping("/test2")
    public ModelAndView testView() {
        ModelAndView mv = new ModelAndView("test");
        mv.addObject("nameKey", path);
        mv.addObject("11111", "1111");
        mv.addObject("11111", "111111");
        mv.addObject("sssss", "sssssss");
        mv.addObject("ssscccc", "sssssss");
        return mv;
    }

    @RequestMapping("/test3")
    public ModelAndView test3() {
        Map<String, String> map = new HashMap();
        map.put("aaaa", "aaaaa");
        map.put("bbbb", "bbbbb");
        map.put("cccc", "你俩的禄口机场斯蒂芬路上慢点");
        map.put("nameKey", path);
        return new ModelAndView("test", map);
    }


    @RequestMapping("/redis")
    public String redis() {
        redisTemplate.opsForValue().set("aaa", "ssssss");
        return "test";
    }


}

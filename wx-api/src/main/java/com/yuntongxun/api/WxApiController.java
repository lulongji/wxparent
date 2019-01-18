package com.yuntongxun.api;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.utils.WeChatUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: 微信公众平台接收消息
 * @Author: lulongji
 * @Date: Created in 14:02 2018/11/27
 */
@RestController
@RequestMapping("/wx/api")
public class WxApiController {


    private static final Logger logger = LoggerFactory.getLogger(WxApiController.class);

    /**
     * 创建菜单
     *
     * @param appid
     * @param body
     * @return
     */
    @RequestMapping("/{appid}/createMenu")
    public JSONObject createMenu(@RequestBody String body, @PathVariable("appid") String appid) {
        JSONObject jsonObject = null;
        try {
            if (body == null || "".equals(body)) {
                jsonObject = new JSONObject();
                jsonObject.put("errcode", "-1");
                jsonObject.put("errmsg", "params is null.");
            } else {
                jsonObject = WeChatUtil.createMenu(body, appid);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return jsonObject;
    }


    /**
     * 查询菜单数据
     *
     * @param appid
     * @return
     */
    @RequestMapping("/{appid}/getMenu")
    public JSONObject getMenu(@PathVariable("appid") String appid) {
        JSONObject jsonObject = null;
        try {
            jsonObject = WeChatUtil.getMenu(appid);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            e.printStackTrace();
        }
        return jsonObject;
    }

}

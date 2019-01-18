package com.yuntongxun.api;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.model.properties.AppFilesProperties;
import com.yuntongxun.model.wechat.utils.CommonUtils;
import com.yuntongxun.service.message.receive.WxReceiveMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description: MCM接收消息
 * @Author: lulongji
 * Created by lu on 2018/12/10.
 */

@RestController
@RequestMapping("/mcm/api")
public class McmApiController {

    private static final Logger logger = LoggerFactory.getLogger(McmApiController.class);

    @Autowired
    private WxReceiveMessageService wxReceiveMessageService;

    @Autowired
    private AppFilesProperties appFilesProperties;

    /**
     * 接收mcm消息并发送至微信端
     *
     * @param request
     * @return
     */
    @RequestMapping("/sendWxMessage")
    public String sendWxMessage(HttpServletRequest request) {
        try {
            String retstr = CommonUtils.getRequestStr(request);
            Map<String, Object> requestMap = JSONObject.parseObject(retstr, Map.class);
            requestMap.put("fileServerUri", appFilesProperties.getFileServerPath());
            wxReceiveMessageService.receiveMsg(requestMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject reply = new JSONObject();
        reply.put("statusCode", "000000");
        reply.put("msg", "msg received success");
        return reply.toString();

    }
}

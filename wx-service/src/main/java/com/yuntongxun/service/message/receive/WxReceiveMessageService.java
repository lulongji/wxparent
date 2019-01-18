package com.yuntongxun.service.message.receive;

import com.yuntongxun.base.wechat.common.WxConsts;
import com.yuntongxun.base.wechat.util.EncryptUtil;
import com.yuntongxun.model.wechat.utils.FileUtil;
import com.yuntongxun.utils.WeChatUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newFixedThreadPool;

/**
 * 微信公众平台发送服务
 *
 * @author lu
 */
@Component
public class WxReceiveMessageService {

    private static final Logger logger = LoggerFactory.getLogger(WxReceiveMessageService.class);

    private final ExecutorService pool;

    private WxReceiveMessageService() {
        pool = newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 4 + 1);
    }

    public void receiveMsg(final Map<String, Object> dataMap) {
        pool.execute(() -> {
            StringBuilder stringBuilder = new StringBuilder("receiveMsg#params:");
            try {
                logger.info("\n\n----------------------------[send thread start,send a message to wechat user start]----------------------------");
                try {
                    Set<Map.Entry<String, Object>> set = dataMap.entrySet();
                    for (Map.Entry<String, Object> entry : set) {
                        stringBuilder.append(",{" + entry.getKey() + "=" + entry.getValue() + "}");
                    }
                    logger.info(stringBuilder.toString());
                    String appid = (String) dataMap.get("openID");
                    String access_token = WeChatUtil.getAccessToken(appid);
                    WeChatUtil.replyMessage(access_token, getFormatMcmMsgData(dataMap, access_token));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                logger.error("receiveMsg#run() exception:", e);
                e.printStackTrace();
            } finally {
                logger.info("----------------------------[send thread start,send a message to wechat user end]----------------------------\n\n");
            }

        });
    }


    /**
     * 接收MCM数据格式化
     *
     * @param dataMap
     * @param access_token
     * @return
     * @throws Exception
     */
    public String getFormatMcmMsgData(Map<String, Object> dataMap, String access_token) throws Exception {
        String data = "";
        String msgType = (String) dataMap.get("msgType");
        JSONObject prams = new JSONObject();
        prams.element("touser", dataMap.get("userID"));

        JSONObject ele = new JSONObject();
        if (msgType.equals(WxConsts.XmlMsgType.TEXT)) {
            logger.info("++++ ready to send a text message to user +++++");
            prams.element("msgtype", WxConsts.XmlMsgType.TEXT);
            String content = (String) dataMap.get("content");
            if (StringUtils.isNotBlank(content)) {
                content = EncryptUtil.base64Decoder(content);
            }
            logger.info("content: " + content);
            ele.element("content", content);
            prams.element(WxConsts.XmlMsgType.TEXT, ele);
            data = prams.toString();

        } else if (msgType.equals(WxConsts.XmlMsgType.IMAGE)) {
            logger.info("++++ ready to send a image message to user +++++");
            Map<String, Object> fileResMap = FileUtil.uploadFile((String) dataMap.get("fileServerUri"), (String) dataMap.get("filePath"), access_token, msgType);
            prams.element("msgtype", msgType);
            ele.element("media_id", fileResMap.get("media_id"));
            prams.element(msgType, ele);
            data = prams.toString();
        } else if (msgType.equals(WxConsts.XmlMsgType.VOICE)) {
            logger.info("++++ ready to send a voice message to user +++++");
            Map<String, Object> fileResMap = FileUtil.uploadFile((String) dataMap.get("fileServerUri"), (String) dataMap.get("filePath"), access_token, msgType);
            prams.element("msgtype", msgType);
            ele.element("media_id", fileResMap.get("media_id"));
            prams.element(msgType, ele);
            data = prams.toString();
        } else if (msgType.equals(WxConsts.XmlMsgType.VIDEO)) {
            logger.info("++++ ready to send a video message to user +++++");
            Map<String, Object> fileResMap = FileUtil.uploadFile((String) dataMap.get("fileServerUri"), (String) dataMap.get("filePath"), access_token, msgType);
            prams.element("msgtype", msgType);
            ele.element("media_id", fileResMap.get("media_id"));
            ele.element("thumb_media_id", dataMap.get("thumbMediaId"));
            ele.element("title", dataMap.get("title"));
            ele.element("description", dataMap.get("description"));
            prams.element(msgType, ele);
            data = prams.toString();
        } else if (msgType.equals(WxConsts.XmlMsgType.MUSIC)) {
            logger.info("++++ ready to send a music message to user +++++");
            prams.element("msgtype", msgType);
            ele.element("musicurl", dataMap.get("musicurl"));
            ele.element("hqmusicurl", dataMap.get("hqmusicurl"));
            ele.element("thumb_media_id", dataMap.get("thumbMediaId"));
            ele.element("title", dataMap.get("title"));
            ele.element("description", dataMap.get("description"));
            prams.element(msgType, ele);
            data = prams.toString();
        } else if (msgType.equals(WxConsts.XmlMsgType.NEWS)) {
            logger.info("++++ ready to send a news message to user +++++");
            prams.element("msgtype", msgType);
            ele.element("articles", dataMap.get("articles"));
            prams.element(msgType, ele);
            data = prams.toString();
        } else {
            logger.info("+++++ message type is not support +++++");
            logger.info("msgType is not support！ msgType：" + msgType);
        }
        return data;
    }


}

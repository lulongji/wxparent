package com.yuntongxun.model.wechat.utils;

import com.yuntongxun.base.webchat.aes.WXBizMsgCrypt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;

public class WXUtils {


    /**
     * 动态解密
     *
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @param msg
     * @param appid
     * @param token
     * @param encodingAESKey
     * @return
     */
    public static String msgDecrypt(String timestamp, String nonce, String msgSignature, String msg, String appid, String token, String encodingAESKey) {
        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appid);
            String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
            String fromXML = String.format(format, msg);
            String result = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
            System.out.println("解密后明文: " + result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("解密失败.");
        }
        return null;
    }

    /**
     * 获取xml节点内容
     *
     * @param xmlStr
     * @param nodeName
     * @return
     */
    public static String getXmlNode(String xmlStr, String nodeName) {

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(xmlStr);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList nodelist = root.getElementsByTagName(nodeName);

            String encrypt = nodelist.item(0).getTextContent();
            return encrypt;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("获取xml节点异常.");
        }
        return null;
    }



}

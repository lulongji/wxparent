package com.yuntongxun.model.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.core.util.QuickWriter;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppDriver;
import com.yuntongxun.base.wechat.util.CommonUtil;
import com.yuntongxun.base.wechat.util.XmlUtil;
import com.yuntongxun.base.wechat.util.aes.WXBizMsgCrypt;
import com.yuntongxun.base.wechat.util.aes.WxCryptUtil;
import com.yuntongxun.model.wechat.message.MusicMessage;
import com.yuntongxun.model.wechat.message.NewsMessage;
import com.yuntongxun.model.wechat.message.TextMessage;
import com.yuntongxun.model.wechat.wx.Article;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;


/**
 * @author lu
 */
public class CommonUtils {

    private static Logger logger = LoggerFactory.getLogger(CommonUtils.class);


    /**
     * 创建菜单http请求
     *
     * @param urlPath
     * @param jsonMenu
     * @return
     */
    public static JSONObject httpJson(String urlPath, String jsonMenu) {
        JSONObject jsonMsg = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setDoOutput(true);
            http.setDoInput(true);
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.connect();
            OutputStream os = http.getOutputStream();
            os.write(jsonMenu.getBytes("UTF-8"));
            os.close();

            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] bt = new byte[size];
            is.read(bt);
            String message = new String(bt, "UTF-8");
            jsonMsg = JSONObject.parseObject(message);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonMsg;
    }

    /**
     * 发送post表单请求
     *
     * @param action
     * @param inputStream
     * @param fileName
     * @return
     */
    public static String sendFormPost(String action, InputStream inputStream, String fileName) {
        String result = null;
        HttpsURLConnection con = null;
        try {
            TrustManager[] tm = {new JEEWeiXinX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(action);
            con = (HttpsURLConnection) url.openConnection();
            con.setSSLSocketFactory(ssf);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            String BOUNDARY = "----------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
            StringBuilder sb = new StringBuilder();
            sb.append("--");
            sb.append(BOUNDARY);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + fileName + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");
            byte[] head = sb.toString().getBytes("utf-8");
            OutputStream out = new DataOutputStream(con.getOutputStream());
            out.write(head);
            DataInputStream in = new DataInputStream(inputStream);
            Integer bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = in.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            in.close();
            byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
            out.write(foot);
            out.flush();
            out.close();
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuffer buffer = new StringBuffer();
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (IOException e) {
            logger.error("An exception occurred while sending a post request!！", e);
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 将请求参数转换为xml格式的string
     *
     * @param parameters 请求参数
     * @return
     * @Description：将请求参数转换为xml格式的string
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k)) {
                sb.append("<" + k + ">" + "<![CDATA[" + v + "]]></" + k + ">");
            } else {
                sb.append("<" + k + ">" + v + "</" + k + ">");
            }
        }
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * 返回给微信的参数
     *
     * @param return_code 返回编码
     * @param return_msg  返回信息
     * @return
     * @Description：返回给微信的参数
     */
    public static String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg
                + "]]></return_msg></xml>";
    }

    /**
     * url编码
     *
     * @param source
     * @return
     */
    public static String urlEncodeUTF8(String source) {
        String result = source;
        try {
            result = java.net.URLEncoder.encode(source, "utf-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("Url encoding exception:", e);
            e.printStackTrace();
        }
        return result;
    }


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
    public static String msgDecrypt(String timestamp,
                                    String nonce,
                                    String msgSignature,
                                    String msg,
                                    String appid,
                                    String token,
                                    String encodingAESKey) {
        logger.info("msgDecrypt#params：[timestamp=[{}], nonce=[{}], msgSignature=[{}], msg=[{}], appid=[{}], token=[{}], encodingAESKey=[{}]",
                timestamp, nonce, msgSignature, msg, appid, token, encodingAESKey);

        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appid);
            String format = "<xml><ToUserName><![CDATA[toUser]]></ToUserName><Encrypt><![CDATA[%1$s]]></Encrypt></xml>";
            String fromXML = String.format(format, msg);
            String result = pc.decryptMsg(msgSignature, timestamp, nonce, fromXML);
            logger.info("Decrypted plaintext:" + result);
            return result;
        } catch (Exception e) {
            logger.error("Decryption failed and an exception occurred:", e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param requestBody
     * @param timestamp
     * @param nonce
     * @param msgSignature
     * @param msg
     * @param appid
     * @param token
     * @param encodingAESKey
     * @return
     */
    public static String msgDecrypt(String requestBody,
                                    String timestamp,
                                    String nonce,
                                    String msgSignature,
                                    String msg,
                                    String appid,
                                    String token,
                                    String encodingAESKey) {
        logger.info("msgDecrypt#params：[timestamp=[{}], nonce=[{}], msgSignature=[{}], msg=[{}], appid=[{}], token=[{}], encodingAESKey=[{}]",
                timestamp, nonce, msgSignature, msg, appid, token, encodingAESKey);

        try {
            WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appid);
            String fromXML = WxCryptUtil.extractEncryptPart(requestBody);
            String result = pc.decryptMsgData(msgSignature, timestamp, nonce, fromXML);
            logger.info("Decrypted plaintext:" + result);
            return result;
        } catch (Exception e) {
            logger.error("Decryption failed and an exception occurred:", e);
            e.printStackTrace();
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
            logger.error("Get XML node exception exception appears exception:", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析微信发来的请求（XML）
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static Map<String, String> parseXml(HttpServletRequest request) throws Exception {
        BufferedReader reader = null;
        reader = request.getReader();
        String line = null;
        String xmlString = null;
        StringBuffer inputString = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            inputString.append(line);
        }
        xmlString = inputString.toString();
        request.getReader().close();
        logger.info("Parse the request (XML) from WeChat and receive the following data:" + xmlString);
        return XmlUtil.doXMLParse(xmlString);
    }

    /**
     * 文本消息对象转换成xml
     *
     * @param textMessage 文本消息对象
     * @return xml
     */
    public static String textMessageToXml(TextMessage textMessage) {
        xstream.alias("xml", textMessage.getClass());
        return xstream.toXML(textMessage);
    }

    /**
     * 音乐消息对象转换成xml
     *
     * @param musicMessage 音乐消息对象
     * @return xml
     */
    public static String musicMessageToXml(MusicMessage musicMessage) {
        xstream.alias("xml", musicMessage.getClass());
        return xstream.toXML(musicMessage);
    }

    /**
     * 图文消息对象转换成xml
     *
     * @param newsMessage 图文消息对象
     * @return xml
     */
    public static String newsMessageToXml(NewsMessage newsMessage) {
        xstream.alias("xml", newsMessage.getClass());
        xstream.alias("item", new Article().getClass());
        return xstream.toXML(newsMessage);
    }

    /**
     * 扩展xstream，使其支持CDATA块
     */
    private static XStream xstream = new XStream(new XppDriver() {
        @Override
        public HierarchicalStreamWriter createWriter(Writer out) {
            return new PrettyPrintWriter(out) {
                // 对所有xml节点的转换都增加CDATA标记
                boolean cdata = true;

                @Override
                public void startNode(String name, @SuppressWarnings("rawtypes") Class clazz) {
                    super.startNode(name);
                }

                @Override
                protected void writeText(QuickWriter writer, String text) {
                    if (cdata) {
                        writer.write("<![CDATA[");
                        writer.write(text);
                        writer.write("]]>");
                    } else {
                        writer.write(text);
                    }
                }
            };
        }
    });


    /**
     * 获取请求的body数据
     *
     * @param request
     * @return
     */
    public static String getRequestStr(HttpServletRequest request) {
        StringBuffer sb = new StringBuffer();
        String retstr = null;
        try {
            request.setCharacterEncoding("UTF-8");
            InputStream is = request.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            retstr = sb.toString();
        } catch (Exception e) {
            logger.error("An exception occurred getting the requested body data:", e.getMessage(), e);
            e.printStackTrace();
        }
        return retstr;
    }


    /**
     * 获取redis 常量配置
     *
     * @param key   redis常量
     * @param appid 公众号appid
     * @return
     */
    public static String getRedisConstant(String key, String appid) {
        return key + appid;

    }

    /**
     * 下载文件
     *
     * @param fileUrl
     * @param storefile
     */
    public static void getData(String fileUrl, File storefile) throws Exception {
        HttpClient client = new HttpClient();
        GetMethod getMethod = new GetMethod(fileUrl);
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            client.executeMethod(getMethod);
            InputStream in = getMethod.getResponseBodyAsStream();
            bos = new BufferedOutputStream(new FileOutputStream(storefile));
            bis = new BufferedInputStream(in);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("File save failed!");
        } finally {
            bos.close();
            bis.close();
        }
    }

    /**
     * md5
     *
     * @param userName
     * @param password
     * @return
     */
    public static String getFileServerSig(String userName, String password) {
        String sig = null;
        try {
            sig = CommonUtil.md5(userName + password).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            logger.info("md5 msg error:" + e.getMessage());
        }
        return sig;
    }

}

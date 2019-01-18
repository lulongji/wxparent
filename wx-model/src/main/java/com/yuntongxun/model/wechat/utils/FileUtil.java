package com.yuntongxun.model.wechat.utils;

import com.alibaba.fastjson.JSONObject;
import com.yuntongxun.base.uuid.UUIDGenerator;
import com.yuntongxun.base.wechat.common.WxConsts;
import com.yuntongxun.base.wechat.util.HttpClientUtils;
import com.yuntongxun.model.constants.WxConstant;
import com.yuntongxun.model.properties.AppFilesProperties;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件服务
 */
public class FileUtil {

    private static final Logger logger = LoggerFactory.getLogger(FileUtil.class);


    /**
     * 上传文件到文件服务器
     *
     * @param fileUrl
     * @param msgType
     * @param ronglianappid
     * @param FromUserName
     * @return
     * @throws Exception
     */
    public static String uploadFile(String fileUrl, String msgType, String ronglianappid,
                                    String FromUserName, AppFilesProperties appFilesProperties) throws Exception {

        String fileId = new UUIDGenerator().generate();
        File folder = new File(appFilesProperties.getFileTempDir());
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File folder1 = new File("/app/im_attach/wx_temp_mp3");
        if (!folder1.exists()) {
            folder1.mkdirs();
        }
        String localPath = appFilesProperties.getFileTempDir() + "/" + fileId;
        File storefile = new File(localPath);
        CommonUtils.getData(fileUrl, storefile);

        FileInputStream fit = null;
        try {
            // 将文件保存至文件服务器
            String finalUrl = getFinalUrl(msgType, ronglianappid, FromUserName,
                    appFilesProperties.getFileServerPath(), appFilesProperties.getFileServerUpload(), appFilesProperties.getUsername(),
                    appFilesProperties.getPassword());
            byte[] buffer;
            if (WxConsts.XmlMsgType.VOICE.equals(msgType)) {
                if (amrToMp3(localPath, fileId)) {
                    File storefile2 = new File(localPath + ".mp3");
                    CommonUtils.getData(appFilesProperties.getMp3Url() + "/" + fileId + ".mp3", storefile2);
                    fit = new FileInputStream(storefile2);
                    buffer = new byte[(int) storefile2.length()];
                } else {
                    throw new Exception("Wechat amrToMp3 failer!");
                }
            } else {
                fit = new FileInputStream(storefile);
                buffer = new byte[(int) storefile.length()];
            }
            int offset = 0;
            int numRead = 0;
            while (offset < buffer.length && (numRead = fit.read(buffer, offset, buffer.length - offset)) >= 0) {
                offset += numRead;
            }
            // 将文件上传至文件服务器
            String responseData = HttpClientUtils.post(finalUrl, buffer, 60000);
            logger.info("responseData:" + responseData);
            String downloadurl = null;
            if (responseData != null && responseData.length() > 0) {
                FileServerResponseData fileServerResponseData = JSONObject.parseObject(responseData, FileServerResponseData.class);
                if (fileServerResponseData != null) {
                    downloadurl = fileServerResponseData.getDownloadUrl();
                }
            }
            if (StringUtils.isEmpty(downloadurl)) {
                logger.info("save file to file server error,response download url is empty!");
            }
            return downloadurl;
        } finally {
            if (fit != null) {
                fit.close();
            }
        }

    }


    /**
     * 获取最终文件路径
     *
     * @param msgType
     * @param ronglianappid
     * @param FromUserName
     * @return
     */
    public static String getFinalUrl(String msgType, String ronglianappid, String FromUserName,
                                     String fileServerPath, String fileUploadPath, String username, String password) {
        String uploadFileName = new UUIDGenerator().generate();
        if (WxConsts.XmlMsgType.VOICE.equals(msgType)) {
            uploadFileName = uploadFileName + ".mp3";
        } else if (WxConsts.XmlMsgType.VIDEO.equals(msgType) ||
                WxConsts.XmlMsgType.SHORTVIDEO.equals(msgType)) {
            uploadFileName = uploadFileName + ".mp4";
        } else if (WxConsts.XmlMsgType.IMAGE.equals(msgType)) {
            uploadFileName = uploadFileName + ".jpg";
        }
        return fileServerPath + fileUploadPath + "?appId=" + ronglianappid + "&userName="
                + FromUserName + "&fileName="
                + uploadFileName + "&sig=" + CommonUtils.getFileServerSig(username, password)
                + "&msgType=" + msgType;
    }


    /**
     * @param localPath
     * @param targetFilePath
     * @return boolean
     * @Title amrToMp3
     * @Description amr 文件转换 MP3
     * @date 2018年5月11日 下午7:28:33
     * @version v.dev
     */
    public static boolean amrToMp3(String localPath, String targetFilePath) {
        try {
            logger.info("************** ffmpeg begin ****************");
            java.lang.Runtime rt = Runtime.getRuntime();
            String command = "ffmpeg -i " + localPath + " "
                    + targetFilePath + ".mp3";
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuffer sb = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            logger.info("ffmpeg Process errorInfo: " + sb.toString());
            int exitVal = proc.waitFor();
            logger.info("ffmpeg Process exitValue: " + exitVal);

            logger.info("Start moving files.");
            String command2 = "mv " + targetFilePath + ".mp3" + " /app/im_attach/wx_temp_mp3";
            Process proc2 = rt.exec(command2);
            InputStream stderr2 = proc2.getErrorStream();
            InputStreamReader isr2 = new InputStreamReader(stderr2);
            BufferedReader br2 = new BufferedReader(isr2);
            String line2 = null;
            StringBuffer sb2 = new StringBuffer();
            while ((line2 = br2.readLine()) != null) {
                sb.append(line2);
            }
            logger.info("mv Process errorInfo: " + sb2.toString());
            int exitVal2 = proc2.waitFor();
            logger.info("mv Process exitValue: " + exitVal2);

            return true;
        } catch (Exception e) {
            logger.error("mv exec cmd exception:", e);
        }
        return false;
    }

    /**
     * 上传文件素材
     *
     * @param filePath
     * @param accessToken
     * @param msgType
     * @return
     * @throws Exception
     */
    public static Map<String, Object> uploadFile(String fileServerUri, String filePath, String accessToken, String msgType) throws Exception {
        // 根据文件下载地址下载文件
        URL url = new URL(fileServerUri + filePath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // 设置超时间为3秒
        conn.setConnectTimeout(5 * 1000);
        // 防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        // 得到输入流
        InputStream inputStream = conn.getInputStream();
        // 上传文件请求路径
        String action = WxConstant.ADD_MATERIAL_URL.replace("[ACCESS_TOKEN]", accessToken).replace("[TYPE]", msgType);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        String result = CommonUtils.sendFormPost(action, inputStream, fileName);
        logger.info("调用微信上传文件接口返回结果  result:" + result);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("flag", false);
        if (StringUtils.isNotBlank(result)) {
            try {
                data = JSONObject.parseObject(result, Map.class);
                data.put("flag", true);
            } catch (Exception e1) {
                data.put("msg", "响应json数据解析异常！");
                return data;
            }
        } else {
            data.put("msg", "响应数据为空！");
        }
        return data;
    }


}



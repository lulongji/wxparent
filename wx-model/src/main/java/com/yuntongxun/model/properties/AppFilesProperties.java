package com.yuntongxun.model.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Description: 文件服务器基础常量
 * @Author: lu
 * @Date: Created in 17:05 2018/11/28
 */

@Data
@ConfigurationProperties(prefix = "app.file")
public class AppFilesProperties {

    private String fileTempDir;

    private String mp3Url;

    private String fileServerPath;

    private String fileServerUpload;

    private String username;

    private String password;
}

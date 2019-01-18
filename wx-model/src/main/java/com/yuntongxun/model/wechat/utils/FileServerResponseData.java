package com.yuntongxun.model.wechat.utils;

import lombok.Data;


@Data
public class FileServerResponseData {
    private String statusCode;
    private String downloadUrl;
    private String fileName;

}

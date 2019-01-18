package com.yuntongxun.common.exception;

import com.yuntongxun.base.constants.CommonConstants;

/**
 * @Description: 自定义异常
 * @Author: lu
 * @Date: Created in 10:09 2018/9/29
 */
public enum ExceptionEnum {
    /**
     * An unknown error.
     */
    UNKONW_ERROR(CommonConstants.ValType.UNKNOWN_ERROR, "An unknown error."),
    /**
     * 成功
     */
    SUCCESS(CommonConstants.ValType.SUCCESS_CODE, CommonConstants.ValType.FAILURE_CODE),;

    private String code;

    private String info;

    ExceptionEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

    public String getCode() {

        return code;
    }

    public String getInfo() {
        return info;
    }
}
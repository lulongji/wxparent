package com.yuntongxun.common.exception;


import com.yuntongxun.base.bean.Result;
import com.yuntongxun.base.constants.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description: 自定义异常
 * @Author: lu
 * @Date: Created in 10:04 2018/9/29
 */
@ControllerAdvice
public class ExceptionHandle {

    private final static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandle.class);


    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result catchFromService(Exception e) {
        if (e instanceof BootException) {
            BootException bootException = (BootException) e;
            return Result.error(CommonConstants.ValType.FAILURE_CODE, bootException.getMessage());
        }
        LOGGER.error("【A system exception】{}", e);
        return resultError(ExceptionEnum.UNKONW_ERROR);
    }


    /**
     * @param exceptionEnum
     * @return
     */
    public Result resultError(ExceptionEnum exceptionEnum) {
        return Result.error(exceptionEnum.getCode(), exceptionEnum.getInfo());
    }
}
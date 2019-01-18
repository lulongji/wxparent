package com.yuntongxun.service.message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.util.concurrent.Executors.*;

/**
 * @Author: lu
 * @Date: Created in 11:41 2018/11/17
 */
public class WxMessageFixedThreadPool {
    private static final Logger logger = LoggerFactory.getLogger(WxMessageFixedThreadPool.class);

    private static ExecutorService fixedThreadPool;

    public WxMessageFixedThreadPool(int fixedThreadPoolNum) {
        logger.info("Number of threads the number of pool allocations：" + fixedThreadPoolNum);
        fixedThreadPool = newFixedThreadPool(fixedThreadPoolNum + 1);
    }

    public static void addTask(WxMessageTask messageTask) {
        logger.info("Current thread count：" + ((ThreadPoolExecutor) fixedThreadPool).getActiveCount());
        fixedThreadPool.execute(messageTask);
    }
}

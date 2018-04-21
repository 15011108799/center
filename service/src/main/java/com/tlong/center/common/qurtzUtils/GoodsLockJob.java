package com.tlong.center.common.qurtzUtils;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GoodsLockJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(GoodsLockJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务默认执行方法!");
        //这里需要去执行具体的业务逻辑这里是开启定时任务  锁定商品的状态
    }
}

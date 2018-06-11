package com.tlong.center.common.qurtzUtils;

import com.tlong.center.service.WebGoodsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class GoodsLockJob implements Job {

    private final Logger logger = LoggerFactory.getLogger(GoodsLockJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("定时任务默认执行方法!");
        WebGoodsService webGoodsService = (WebGoodsService) jobExecutionContext.getJobDetail().getJobDataMap().get("service");
        webGoodsService.updateState((Long) jobExecutionContext.getJobDetail().getJobDataMap().get("goodsId"), 2);
    }
}

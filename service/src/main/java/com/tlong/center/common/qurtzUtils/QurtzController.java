package com.tlong.center.common.qurtzUtils;

import com.tlong.center.api.dto.quertz.QuartzRequestDto;
import com.tlong.center.service.WebGoodsService;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz定时任务Util
 */
@RestController
@RequestMapping("/api/quartz")
public class QurtzController {

    private final Logger logger = LoggerFactory.getLogger(QurtzController.class);
    final WebGoodsService webGoodsService;

    public QurtzController(WebGoodsService webGoodsService) {
        this.webGoodsService = webGoodsService;
    }

    @PostMapping("/main")
    public void testSchedulerTask(@RequestBody QuartzRequestDto requestDto) throws SchedulerException {
        //创建默认定时任务对象
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = this.creatGoodsLockJob(requestDto);
        Trigger trigger = this.creatGoodsLockTrigger();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
        try {
            Long laterTime = 7200L;
            if (requestDto.getLaterTime() != null) {
                laterTime = requestDto.getLaterTime();
            }
            Thread.sleep(laterTime);
            //这里去调用业务的技术逻辑
            Long goodsId = null;
            if (requestDto.getGoodsId() != null) {
                goodsId = this.unLockGoods(requestDto.getGoodsId());
            }
            if (goodsId != null) {
                logger.info("商品id为" + goodsId + "的商品已被成功解除锁定");
            }
            scheduler.shutdown(true);
            logger.info("某个定时任务被关闭");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    /**
     * 创建商品上锁30分钟后自动解锁的定时任务
     */
    public JobDetail creatGoodsLockJob(QuartzRequestDto requestDto) {
        //定义工作并将其与我们的GoodsLockJob类联系起来
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("goodsId", requestDto.getGoodsId());
        jobDataMap.put("service", webGoodsService);
        return newJob(GoodsLockJob.class)
                .withIdentity("goodsLockJob", "GoodsLockGroup")
                .setJobData(jobDataMap)
                .build();
    }

    public Trigger creatGoodsLockTrigger() {
        return newTrigger()
                .withIdentity("trigger1", "GoodsLockGroup")
                .startAt(this.localDateTimeToDate(LocalDateTime.now()))
                .endAt(this.localDateTimeToDate(LocalDateTime.now().plusMinutes(30)))
                .build();
    }

    public Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        logger.info("One New Lock Task Is Running At:" + date);
        return date;
    }


    /**
     * 解除商品上锁
     */
    public Long unLockGoods(Long goodsId) {
        //修改商品的状态 TODO
        webGoodsService.updateState(goodsId, 1);
        logger.info("商品" + goodsId + "已经被解锁");
        return goodsId;
    }

}

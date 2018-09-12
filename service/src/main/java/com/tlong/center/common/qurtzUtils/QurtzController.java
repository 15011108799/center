package com.tlong.center.common.qurtzUtils;

import com.tlong.center.api.dto.quertz.QuartzRequestDto;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.common.quartz.QTlongQuartz;
import com.tlong.center.domain.common.quartz.TlongQuartz;
import com.tlong.center.domain.repository.AppGoodsRepository;
import com.tlong.center.domain.repository.TlongQuartzRepository;
import com.tlong.center.domain.repository.WebOrderRepository;
import com.tlong.center.domain.web.QWebOrder;
import com.tlong.center.domain.web.WebOrder;
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
import java.util.Objects;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Quartz定时任务Util
 */
@RestController
@RequestMapping("/api/quartz")
public class QurtzController {

    private final Logger logger = LoggerFactory.getLogger(QurtzController.class);
    private final WebGoodsService webGoodsService;
    private final WebOrderRepository webOrderRepository;
    private final AppGoodsRepository goodsRepository;
    private final TlongQuartzRepository quartzRepository;

    public QurtzController(WebGoodsService webGoodsService, WebOrderRepository webOrderRepository, AppGoodsRepository goodsRepository, TlongQuartzRepository quartzRepository) {
        this.webGoodsService = webGoodsService;
        this.webOrderRepository = webOrderRepository;
        this.goodsRepository = goodsRepository;
        this.quartzRepository = quartzRepository;
    }

    @PostMapping("/main")
    public Long testSchedulerTask(@RequestBody QuartzRequestDto requestDto) throws SchedulerException {
        //创建默认定时任务对象
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();

        JobDetail jobDetail = this.creatGoodsLockJob(requestDto);
        Trigger trigger = this.creatGoodsLockTrigger();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
//        Long laterTime = 7200000L;
        Long laterTime = 30000L;
        TlongQuartz one1 = quartzRepository.findOne(QTlongQuartz.tlongQuartz.goodsId.eq(requestDto.getGoodsId()));
        if (Objects.isNull(one1)){
            TlongQuartz quartz = new TlongQuartz();
            quartz.setGoodsId(requestDto.getGoodsId());
            quartzRepository.save(quartz);
            logger.info("定时任务商品持久化完成!");
        }

        try {
//            Long laterTime = 1L;
            if (requestDto.getLaterTime() != null) {
                laterTime = requestDto.getLaterTime();
            }
            Thread.sleep(laterTime);
            //这里去调用业务的技术逻辑
            Long goodsId = null;
            if (requestDto.getGoodsId() != null) {
                this.orderStateChange(requestDto.getGoodsId());
                goodsId = this.unLockGoods(requestDto.getGoodsId());
            }
            if (goodsId != null) {
                TlongQuartz one = quartzRepository.findOne(QTlongQuartz.tlongQuartz.goodsId.eq(goodsId));
                if (Objects.nonNull(one)){
                    quartzRepository.delete(one);
                }
                logger.info("商品id为" + goodsId + "的商品已被成功解除锁定");
            }
            scheduler.shutdown(true);
            logger.info("某个定时任务被关闭");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return laterTime;

    }


    /**
     * 创建商品上锁30分钟后自动解锁的定时任务
     */
    private JobDetail creatGoodsLockJob(QuartzRequestDto requestDto) {
        //定义工作并将其与我们的GoodsLockJob类联系起来
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("goodsId", requestDto.getGoodsId());
        jobDataMap.put("service", webGoodsService);
        return newJob(GoodsLockJob.class)
                .withIdentity("goodsLockJob", "GoodsLockGroup")
                .setJobData(jobDataMap)
                .build();
    }

    private Trigger creatGoodsLockTrigger() {
        return newTrigger()
                .withIdentity("trigger1", "GoodsLockGroup")
                .startAt(this.localDateTimeToDate(LocalDateTime.now()))
                .endAt(this.localDateTimeToDate(LocalDateTime.now().plusMinutes(30)))
                .build();
    }

    private Date localDateTimeToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        Date date = Date.from(zdt.toInstant());
        logger.info("One New Lock Task Is Running At:" + date);
        return date;
    }


    /**
     * 解除商品上锁
     */
    private Long unLockGoods(Long goodsId) {
        //修改商品的状态 TODO
        WebGoods webGoods = goodsRepository.findOne(goodsId);
        webGoods.setState(1);
        webGoods.setCurState(1);
        goodsRepository.save(webGoods);
//        webGoodsService.updateState(goodsId, 1);
        logger.info("商品" + goodsId + "已经被解锁");
        return goodsId;
    }

    /**
     * 修改订单状态为3
     */
    private void orderStateChange(Long goodsId) {
        WebOrder one = webOrderRepository.findOne(QWebOrder.webOrder.goodsId.eq(goodsId)
            .and(QWebOrder.webOrder.state.in(1)));
        one.setState(3);
        webOrderRepository.save(one);
    }

}

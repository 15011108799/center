package com.tlong.center.event;

import com.tlong.center.api.dto.quertz.QuartzRequestDto;
import com.tlong.center.common.qurtzUtils.QurtzController;
import com.tlong.center.domain.common.quartz.TlongQuartz;
import com.tlong.center.domain.repository.TlongQuartzRepository;
import org.apache.commons.collections.CollectionUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final TlongQuartzRepository quartzRepository;
    private final QurtzController qurtzController;

    @Autowired
    public MyApplicationRunner(TlongQuartzRepository quartzRepository, QurtzController qurtzController) {
        this.quartzRepository = quartzRepository;
        this.qurtzController = qurtzController;
    }

    @Override
    public void run(ApplicationArguments applicationArguments) {
        System.out.println("application starting .....");
        //获取数据库中的定时任务商品
        List<TlongQuartz> all = quartzRepository.findAll();
        if (CollectionUtils.isNotEmpty(all)){
            for (TlongQuartz one : all) {
                QuartzRequestDto requestDto = new QuartzRequestDto();
                requestDto.setGoodsId(one.getGoodsId());
                try {
                    qurtzController.testSchedulerTask(requestDto);
                } catch (SchedulerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

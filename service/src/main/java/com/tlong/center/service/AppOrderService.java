package com.tlong.center.service;

import com.tlong.center.api.dto.app.order.AddOrderRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.quertz.QuartzRequestDto;
import com.tlong.center.common.qurtzUtils.QurtzController;
import com.tlong.center.domain.repository.WebOrderRepository;
import com.tlong.center.domain.web.WebOrder;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Date;

@Component
@Transactional
public class AppOrderService {

    private final WebOrderRepository webOrderRepository;
    private final WebGoodsService webGoodsService;
    private final QurtzController qurtzController;

    public AppOrderService(WebOrderRepository webOrderRepository, WebGoodsService webGoodsService, QurtzController qurtzController) {
        this.webOrderRepository = webOrderRepository;
        this.webGoodsService = webGoodsService;
        this.qurtzController = qurtzController;
    }

    /**
     * 下单
     */
    public TlongResultDto add(AddOrderRequestDto requestDto) {
        WebOrder webOrder = new WebOrder();
        webOrder.setGoodsId(requestDto.getGoodsId());
        webOrder.setUserId(requestDto.getUserId());
        webOrder.setState(1);
        webOrder.setCreateTime(new Date());
        webOrderRepository.save(webOrder);
        //修改商品表中的商品状态
        QuartzRequestDto quartzRequestDto = new QuartzRequestDto();
        quartzRequestDto.setLaterTime(7200L);
        quartzRequestDto.setGoodsId(requestDto.getGoodsId());
        try {
            qurtzController.testSchedulerTask(quartzRequestDto);
        } catch (SchedulerException e) {
            e.printStackTrace();
            return new TlongResultDto(1,"下单失败,创建定时任务出错!");
        }
        return new TlongResultDto(0,"下单成功！商品已经被锁定，锁定时长：7200M");
    }
}

package com.tlong.center.app.order;

import com.tlong.center.api.app.order.OrderApi;
import com.tlong.center.api.dto.app.order.AddOrderRequestDto;
import com.tlong.center.api.dto.app.order.OrderOverRequestDto;
import com.tlong.center.api.dto.app.order.OrderOverResponseDto;
import com.tlong.center.api.dto.app.order.OrderSmallDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.AppOrderService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/order")
public class OrderController implements OrderApi {


    private final AppOrderService appOrderService;

    public OrderController(AppOrderService appOrderService) {
        this.appOrderService = appOrderService;
    }

    /**
     * 下单
     */
    @Override
    public TlongResultDto add(@RequestBody AddOrderRequestDto requestDto) {
        return appOrderService.add(requestDto);
}

    /**
     * 已结缘商品列表
     */
    @Override
    public Page<OrderSmallDto> orderOverLists(@RequestBody OrderOverRequestDto requestDto) {
        return appOrderService.orderOverLists(requestDto);
    }
}

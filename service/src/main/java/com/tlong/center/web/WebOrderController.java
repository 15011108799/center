package com.tlong.center.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebOrderApi;
import com.tlong.center.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/order")
public class WebOrderController implements WebOrderApi {

    @Autowired
    private OrderService orderService;

    @Override
    public PageResponseDto<OrderRequestDto> findAllOrders(PageAndSortRequestDto requestDto) {
        return orderService.findAllOrders(requestDto);
    }
}

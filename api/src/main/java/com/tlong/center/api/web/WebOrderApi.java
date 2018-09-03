package com.tlong.center.api.web;

import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.order.OrderSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.OrderPageRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Api("订单管理接口")
public interface WebOrderApi {

    @ApiOperation("查询所有订单接口")
    @PostMapping("/findAllOrders")
    PageResponseDto<OrderRequestDto> findAllOrders(@RequestBody OrderPageRequestDto requestDto);

    @ApiOperation("条件查询所有订单接口")
    @PostMapping("/searchOrders")
    PageResponseDto<OrderRequestDto> searchOrders(@RequestBody OrderSearchRequestDto requestDto, HttpSession session);

}

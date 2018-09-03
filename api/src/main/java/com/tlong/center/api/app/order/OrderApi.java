package com.tlong.center.api.app.order;

import com.tlong.center.api.dto.app.order.AddOrderRequestDto;
import com.tlong.center.api.dto.app.order.OrderOverRequestDto;
import com.tlong.center.api.dto.app.order.OrderSmallDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("订单接口")
public interface OrderApi {

    @ApiOperation("下单")
    @PostMapping("")
    TlongResultDto add(@RequestBody AddOrderRequestDto requestDto);

    @ApiOperation("获取订单信息")
    @PostMapping("/orderList")
    Page<OrderSmallDto> orderOverLists(@RequestBody OrderOverRequestDto requestDto);
}

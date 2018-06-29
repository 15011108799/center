package com.tlong.center.api.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.order.OrderSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.EvaluateRequestDto;
import com.tlong.center.api.dto.web.EvaluateSearchRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Api("评价管理接口")
public interface WebEvaluateApi {

    @ApiOperation("查询所有评价接口")
    @PostMapping("/findAllEvaluate")
    PageResponseDto<EvaluateRequestDto> findAllEvaluate(@RequestBody PageAndSortRequestDto requestDto, HttpSession session);

    @ApiOperation("条件查询所有评论接口")
    @PostMapping("/searchEvaluate")
    PageResponseDto<EvaluateRequestDto> searchEvaluate(@RequestBody EvaluateSearchRequestDto requestDto, HttpSession session);

}

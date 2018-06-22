package com.tlong.center.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.EvaluateRequestDto;
import com.tlong.center.api.web.WebEvaluateApi;
import com.tlong.center.api.web.WebOrderApi;
import com.tlong.center.service.EvaluateService;
import com.tlong.center.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/web/evaluate")
public class WebEvaluateController implements WebEvaluateApi {

    @Autowired
    private EvaluateService evaluateService;

    @Override
    public PageResponseDto<EvaluateRequestDto> findAllEvaluate(@RequestBody PageAndSortRequestDto requestDto, HttpSession session) {
        return evaluateService.findAllEvaluate(requestDto,session);
    }
}

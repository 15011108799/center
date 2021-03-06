package com.tlong.center.api.web;

import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpSession;

@Api("Web登录接口")
public interface WebLoginApi {
    @PostMapping("/login")
    @ApiOperation("web登录页面")
    WebLoginResponseDto webLogin(@RequestBody WebLoginRequestDto requestDto, HttpSession session);
}

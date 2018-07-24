package com.tlong.center.api.app;

import com.tlong.center.api.dto.app.clazz.ClazzResponseDto;
import com.tlong.center.api.dto.app.clazz.ClazzStyleResponseDto;
import com.tlong.center.api.dto.app.user.AppUserLoginRequestDto;
import com.tlong.center.api.dto.app.user.AppUserLoginResponseDto;
import com.tlong.center.api.dto.app.user.AppUserResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api("APP基本接口")
public interface AppBaseApi {

    @ApiOperation("APP登录接口")
    @PostMapping("/login")
    AppUserLoginResponseDto appLogin(@RequestBody AppUserLoginRequestDto requestDto);

    @ApiOperation("根据id获取用户信息")
    @PostMapping("/userInfo/{userId}")
    AppUserResponseDto userInfo(@PathVariable Long userId);

    //供应商
    @ApiOperation("获取下级代理商")
    @PostMapping("/children/{userId}")
    List<AppUserResponseDto> children(@PathVariable Long userId);

    @ApiOperation("天珑学院列表")
    @GetMapping("/clazzStyles")
    List<ClazzStyleResponseDto> clazzStyles();

    @ApiOperation("根据课程id获取课程列表")
    @PostMapping("/clazzList/{clazzId}")
    List<ClazzResponseDto> clazzList(@PathVariable Long clazzId);

    //消息通知  复用-- com.tlong.center.api.web.WebMessageApi

}

package com.tlong.center.api.app.user;

import com.tlong.center.api.dto.app.user.AppUserRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("APP用户注册")
public interface AppUserApi {

    @ApiOperation("游客用户注册")
    @PostMapping("")
    TlongResultDto addUser(@RequestBody AppUserRequestDto requestDto);
}

package com.tlong.center.api.app;

import com.tlong.center.api.dto.app.AppUpdateCheckRequestDto;
import com.tlong.center.api.dto.app.AppUpdateCheckResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("APP更新推送接口")
public interface AppUpdateApi {

    @ApiOperation("App软件更新推送")
    @PostMapping("/appUpdateCheck")
    AppUpdateCheckResponseDto appUpdateCheck(@RequestBody AppUpdateCheckRequestDto requestDto);
}

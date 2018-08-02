package com.tlong.center.api.common.check;

import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api("手机号验证接口")
public interface PhoneCheckOutApi {

    @ApiOperation("发送手机验证码")
    @PostMapping("/sendMessage")
    TlongResultDto sendMessage(@RequestParam String phone);

    @ApiOperation("校验手机验证码")
    @PostMapping("/checkMessage")
    TlongResultDto checkMessage(@RequestParam String phone, @RequestParam String checkCode);
}

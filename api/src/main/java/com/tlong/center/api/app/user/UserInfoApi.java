package com.tlong.center.api.app.user;

import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api("用户信息接口")
public interface UserInfoApi {

    @ApiOperation("校验用户名是否存在")
    @PostMapping("/checkUserName")
    Integer checkUserName(@RequestParam String userName);

    @ApiOperation("修改用户密码")
    @PutMapping("/{userId}")
    TlongResultDto updateUserPassword(@PathVariable Long userId, @RequestParam String newPassword);

}

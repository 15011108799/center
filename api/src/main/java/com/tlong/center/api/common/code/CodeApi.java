package com.tlong.center.api.common.code;

import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("编号接口")
public interface CodeApi {

    @ApiOperation("修改编号前缀")
    @PutMapping("/updateCodeRule")
    void updateCodeRule(@RequestBody TlongUserSettingsRequestDto req);
}

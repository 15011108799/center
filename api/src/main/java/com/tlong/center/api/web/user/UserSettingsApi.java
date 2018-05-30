package com.tlong.center.api.web.user;

import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("用户基本设置接口")
public interface UserSettingsApi {

    @ApiOperation("修改用户商品发布数量")
    @PutMapping("/updateGoodsReleaseNumber")
    void updateGoodsReleaseNumber(@RequestBody TlongUserSettingsRequestDto req);
}

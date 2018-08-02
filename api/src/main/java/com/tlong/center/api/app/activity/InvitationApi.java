package com.tlong.center.api.app.activity;

import com.tlong.center.api.dto.app.activity.InvitationCodeRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api("邀请码相关活动")
public interface InvitationApi {

    @ApiOperation("获取自己的邀请码")
    @PostMapping("/myInvitationCode")
    InvitationCodeRequestDto myInvitationCode(@RequestParam Long userId);
}

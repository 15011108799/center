package com.tlong.center.api.app.activity;

import com.tlong.center.api.dto.app.activity.AddCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.activity.DeleteCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.activity.InvitationCodeRequestDto;
import com.tlong.center.api.dto.app.coupon.FillInBeInvitationCodeRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("邀请码相关活动")
public interface InvitationApi {

    @ApiOperation("获取自己的邀请码")
    @PostMapping("/myInvitationCode")
    InvitationCodeRequestDto myInvitationCode(@RequestParam Long userId);

    @ApiOperation("填写他人邀请码")
    @PostMapping("/fillInBeInvitationCode")
    TlongResultDto fillInInvitationCode(@RequestBody FillInBeInvitationCodeRequestDto requestDto);



}

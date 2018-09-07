package com.tlong.center.api.dto.app.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("填写其他人邀请码")
public class FillInBeInvitationCodeRequestDto implements Serializable {

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("用户id")
    private Long userId;

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

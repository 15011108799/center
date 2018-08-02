package com.tlong.center.api.dto.app.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("邀请码获取返回数据模型")
public class InvitationCodeRequestDto implements Serializable {

    public InvitationCodeRequestDto() {
    }

    public InvitationCodeRequestDto(String invitationCode, Integer invitationUserCount) {
        this.invitationCode = invitationCode;
        this.invitationUserCount = invitationUserCount;
    }

    @ApiModelProperty("邀请码")
    private String invitationCode;

    @ApiModelProperty("该邀请码已经邀请用户数")
    private Integer invitationUserCount;

    public String getInvitationCode() {
        return invitationCode;
    }

    public void setInvitationCode(String invitationCode) {
        this.invitationCode = invitationCode;
    }

    public Integer getInvitationUserCount() {
        return invitationUserCount;
    }

    public void setInvitationUserCount(Integer invitationUserCount) {
        this.invitationUserCount = invitationUserCount;
    }
}

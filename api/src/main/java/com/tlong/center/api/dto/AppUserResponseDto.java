package com.tlong.center.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP登录返回模型")
public class AppUserResponseDto implements Serializable{
    private static final long serialVersionUID = 6836750200118874212L;

    @ApiModelProperty("处理结果")
    private Integer flag;

    @ApiModelProperty("用户Id")
    private Long UserId;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public AppUserResponseDto() {
    }

    public AppUserResponseDto(Integer flag, Long userId) {
        this.flag = flag;
        UserId = userId;
    }
}

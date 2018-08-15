package com.tlong.center.api.dto.app.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP登录返回模型")
public class AppUserLoginResponseDto implements Serializable{
    private static final long serialVersionUID = 6836750200118874212L;

    @ApiModelProperty("处理结果")
    private Integer flag;

    @ApiModelProperty("用户Id")
    private Long UserId;

    @ApiModelProperty("用户类型 1代理商 2供应商")
    private Integer userType;

    @ApiModelProperty("E签宝认证状态(1已认证 0未认证)")
    private Integer esgin;


    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

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

    public AppUserLoginResponseDto(Integer flag, Long userId, Integer userType) {
        this.flag = flag;
        UserId = userId;
        this.userType = userType;
    }

    public AppUserLoginResponseDto() {

    }
}

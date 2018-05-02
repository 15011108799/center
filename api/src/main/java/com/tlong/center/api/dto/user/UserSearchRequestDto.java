package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("用户查询信息返回模型")
public class UserSearchRequestDto implements Serializable {
    private static final long serialVersionUID = -6773010187440278171L;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("注册时间区间开始时间")
    private LocalDateTime startTime;

    @ApiModelProperty("注册时间区间结束时间")
    private LocalDateTime endTime;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

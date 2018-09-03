package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("查找用户发布数量重新发布数量模型")
public class FindUserPublishNumResponseDto implements Serializable {

    @ApiModelProperty("发布数量")
    private Integer publishNumber;

    @ApiModelProperty("重新发布数量")
    private Integer rePublishNumber;

    public FindUserPublishNumResponseDto(Integer publishNumber, Integer rePublishNumber) {
        this.publishNumber = publishNumber;
        this.rePublishNumber = rePublishNumber;
    }

    public FindUserPublishNumResponseDto() {

    }

    public Integer getPublishNumber() {
        return publishNumber;
    }

    public void setPublishNumber(Integer publishNumber) {
        this.publishNumber = publishNumber;
    }

    public Integer getRePublishNumber() {
        return rePublishNumber;
    }

    public void setRePublishNumber(Integer rePublishNumber) {
        this.rePublishNumber = rePublishNumber;
    }
}

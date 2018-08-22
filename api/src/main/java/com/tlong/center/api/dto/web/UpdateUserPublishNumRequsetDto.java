package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("供应商发布商品 重新发布商品请求模型")
public class UpdateUserPublishNumRequsetDto implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("发布数量")
    private Integer publishNumber;

    @ApiModelProperty("重新发布数量")
    private Integer rePublishNumber;

    @ApiModelProperty("是否是企业")
    private Integer isCompany;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }
}

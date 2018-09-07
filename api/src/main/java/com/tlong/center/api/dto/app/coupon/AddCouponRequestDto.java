package com.tlong.center.api.dto.app.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("添加卡券请求数据模型")
public class AddCouponRequestDto implements Serializable {

    @ApiModelProperty("卡券名称")
    private String couponName;

    @ApiModelProperty("卡券类型")
    private Integer couponType;

    @ApiModelProperty("卡券描述")
    private String couponDetail;

    @ApiModelProperty("卡券效果id")
    private Long couponEffectId;

    @ApiModelProperty("卡券过期时间")
    private Long endTime;

    @ApiModelProperty("卡券发行总数量")
    private Integer countNumber;

    public Long getCouponEffectId() {
        return couponEffectId;
    }

    public void setCouponEffectId(Long couponEffectId) {
        this.couponEffectId = couponEffectId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public String getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(String couponDetail) {
        this.couponDetail = couponDetail;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Integer getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Integer countNumber) {
        this.countNumber = countNumber;
    }
}

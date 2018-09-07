package com.tlong.center.api.dto.app.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("优惠券效果返回数据模型")
public class CouponEffectResponsDto implements Serializable {

    @ApiModelProperty("优惠券效果id")
    private Long id;

    @ApiModelProperty("优惠券效果 0测试免单优惠券 1满100-50 2满100-10")
    private Integer couponEffect;

    @ApiModelProperty("优惠券类型0折扣券 1满减券 2特殊券")
    private Integer couponType;

    @ApiModelProperty("满减效果名称")
    private String effectName;

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCouponEffect() {
        return couponEffect;
    }

    public void setCouponEffect(Integer couponEffect) {
        this.couponEffect = couponEffect;
    }
}

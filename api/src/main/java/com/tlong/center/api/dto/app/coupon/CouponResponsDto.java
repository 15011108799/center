package com.tlong.center.api.dto.app.coupon;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("卡券数据返回模型")
public class CouponResponsDto implements Serializable {

    @ApiModelProperty("卡券id")
    private Long id;

    @ApiModelProperty("卡券类型")
    private Integer couponType;

    @ApiModelProperty("卡券名称")
    private String couponName;

    @ApiModelProperty("卡券描述")
    private String couponDetail;

    @ApiModelProperty("卡券效果")
    private String effectName;

    @ApiModelProperty("卡券剩余库存数量")
    private Integer remainNumber;

    @ApiModelProperty("卡券图片路径")
    private String couponPic;

    @ApiModelProperty("当前状态 0未过期 1已过期")
    private Integer curState;

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public String getCouponPic() {
        return couponPic;
    }

    public void setCouponPic(String couponPic) {
        this.couponPic = couponPic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(String couponDetail) {
        this.couponDetail = couponDetail;
    }

    public Integer getCurState() {
        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }
}

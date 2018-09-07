package com.tlong.center.api.dto.app.activity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel("删除个人账户下的优惠券请求数据模型")
public class DeleteCouponToAccountRequestDto extends AddCouponToAccountRequestDto {

    @ApiModelProperty("消费用户id")
    private Long userId;

    @ApiModelProperty("用户手机号")
    private Long phone;

    @ApiModelProperty("卡券id集合")
    private List<Long> couponIds;

    @Override
    public Long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public List<Long> getCouponIds() {
        return couponIds;
    }

    public void setCouponIds(List<Long> couponIds) {
        this.couponIds = couponIds;
    }
}

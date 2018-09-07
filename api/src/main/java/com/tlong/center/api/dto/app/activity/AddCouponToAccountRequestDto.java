package com.tlong.center.api.dto.app.activity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("添加卡券到自己的账户请求数据模型")
public class AddCouponToAccountRequestDto implements Serializable {

    @ApiModelProperty("卡券id集合")
    private List<Long> couponIdList;

    @ApiModelProperty("用户id")
    private Long userId;


    public List<Long> getCouponIdList() {
        return couponIdList;
    }

    public void setCouponIdList(List<Long> couponIdList) {
        this.couponIdList = couponIdList;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

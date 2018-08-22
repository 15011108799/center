package com.tlong.center.api.dto.app.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("下单请求数据模型")
public class AddOrderRequestDto implements Serializable {

    @ApiModelProperty("下单人id")
    private Long userId;

    @ApiModelProperty("商品id")
    private Long goodsId;

    @ApiModelProperty("下单时间")
    private Date createTime;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}

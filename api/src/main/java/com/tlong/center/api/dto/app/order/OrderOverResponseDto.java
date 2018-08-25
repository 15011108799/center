package com.tlong.center.api.dto.app.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.domain.Page;

import java.util.List;

@ApiModel("已结缘商品列表返回模型")
public class OrderOverResponseDto{

    @ApiModelProperty("订单列表")
    Page<OrderSmallDto> orderLists;

    @ApiModelProperty("售出总数量")
    private Integer count;

    @ApiModelProperty("总发布价格")
    private Double countPublishPrice;

    @ApiModelProperty("总创始旗舰店价格")
    private Double countFlagshipPrice;

    public Page<OrderSmallDto> getOrderLists() {
        return orderLists;
    }

    public void setOrderLists(Page<OrderSmallDto> orderLists) {
        this.orderLists = orderLists;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Double getCountPublishPrice() {
        return countPublishPrice;
    }

    public void setCountPublishPrice(Double countPublishPrice) {
        this.countPublishPrice = countPublishPrice;
    }

    public Double getCountFlagshipPrice() {
        return countFlagshipPrice;
    }

    public void setCountFlagshipPrice(Double countFlagshipPrice) {
        this.countFlagshipPrice = countFlagshipPrice;
    }
}

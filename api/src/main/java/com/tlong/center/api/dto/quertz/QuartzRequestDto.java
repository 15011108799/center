package com.tlong.center.api.dto.quertz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("定时任务请求数据模型")
public class QuartzRequestDto {

    @ApiModelProperty("延迟调用时间(毫秒)")
    private Long laterTime;

    @ApiModelProperty("商品id")
    private Long goodsId;

    public Long getLaterTime() {
        return laterTime;
    }

    public void setLaterTime(Long laterTime) {
        this.laterTime = laterTime;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }
}

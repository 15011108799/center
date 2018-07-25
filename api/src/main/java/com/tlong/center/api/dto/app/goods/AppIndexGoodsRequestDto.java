package com.tlong.center.api.dto.app.goods;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("首页商品展示模型")
public class AppIndexGoodsRequestDto extends PageAndSortRequestDto {

    @ApiModelProperty("商品分类id")
    private Long goodsClassId;

    public Long getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Long goodsClassId) {
        this.goodsClassId = goodsClassId;
    }
}

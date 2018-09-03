package com.tlong.center.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("商品类型返回模型模型")
public class GoodsTypeResponseDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品类型名称")
    private String goodsClassName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsClassName() {
        return goodsClassName;
    }

    public void setGoodsClassName(String goodsClassName) {
        this.goodsClassName = goodsClassName;
    }
}

package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("商品分类返回数据模型")
public class GoodsClassRequestDto implements Serializable{
    @ApiModelProperty("一级分类列表")
    private List<WebGoodsClassRequestDto> goodsClassOneDtos;

    @ApiModelProperty("二级分类列表")
    private List<WebGoodsClassRequestDto> goodsClassTwoDtos;

    public List<WebGoodsClassRequestDto> getGoodsClassOneDtos() {
        return goodsClassOneDtos;
    }

    public void setGoodsClassOneDtos(List<WebGoodsClassRequestDto> goodsClassOneDtos) {
        this.goodsClassOneDtos = goodsClassOneDtos;
    }

    public List<WebGoodsClassRequestDto> getGoodsClassTwoDtos() {
        return goodsClassTwoDtos;
    }

    public void setGoodsClassTwoDtos(List<WebGoodsClassRequestDto> goodsClassTwoDtos) {
        this.goodsClassTwoDtos = goodsClassTwoDtos;
    }
}

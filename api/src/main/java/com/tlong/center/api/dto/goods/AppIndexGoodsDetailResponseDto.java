package com.tlong.center.api.dto.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("APP首页商品详情数据模型")
public class AppIndexGoodsDetailResponseDto implements Serializable{
    private static final long serialVersionUID = 2653690795536120658L;

    @ApiModelProperty("商品id")
    private Long id;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品编号")
    private String goodsCode;

    @ApiModelProperty("出厂售价")
    private Double factoryPrice;

    @ApiModelProperty("商品图片URL")
    private List<String> picUrls;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Double getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(Double factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public List<String> getPicUrls() {
        return picUrls;
    }

    public void setPicUrls(List<String> picUrls) {
        this.picUrls = picUrls;
    }
}

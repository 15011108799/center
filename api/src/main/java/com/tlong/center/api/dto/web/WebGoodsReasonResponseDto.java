package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP首页商品驳回原因数据模型")
public class WebGoodsReasonResponseDto implements Serializable {
    private static final long serialVersionUID = 2653690795536120658L;
    @ApiModelProperty("商品id")
    private Long goodsId;
    @ApiModelProperty("商品名称")
    private String goodsHead;
    @ApiModelProperty("商品分类")
    private String goodsClass;
    @ApiModelProperty("商品描述")
    private String des;
    @ApiModelProperty("商品属性")
    private String goodsProperty;
    @ApiModelProperty("商品视频")
    private String video;
    @ApiModelProperty("商品证书")
    private String certificate;
    @ApiModelProperty("商品图片地址")
    private String goodsPic;
    @ApiModelProperty("图片类型")
    private String picType;
    @ApiModelProperty("题材")
    private String theme;
    @ApiModelProperty("圈口大小")
    private String circle;
    @ApiModelProperty("款式")
    private String style;
    @ApiModelProperty("种水")
    private String kindOfWater;
    @ApiModelProperty("颜色")
    private String color;
    @ApiModelProperty("发布价")
    private String publishPrice;
    @ApiModelProperty("数量")
    private String num;
    @ApiModelProperty("价格类型")
    private String priceType;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsHead() {
        return goodsHead;
    }

    public void setGoodsHead(String goodsHead) {
        this.goodsHead = goodsHead;
    }

    public String getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getGoodsProperty() {
        return goodsProperty;
    }

    public void setGoodsProperty(String goodsProperty) {
        this.goodsProperty = goodsProperty;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getPicType() {
        return picType;
    }

    public void setPicType(String picType) {
        this.picType = picType;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getKindOfWater() {
        return kindOfWater;
    }

    public void setKindOfWater(String kindOfWater) {
        this.kindOfWater = kindOfWater;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(String publishPrice) {
        this.publishPrice = publishPrice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPriceType() {
        return priceType;
    }

    public void setPriceType(String priceType) {
        this.priceType = priceType;
    }
}

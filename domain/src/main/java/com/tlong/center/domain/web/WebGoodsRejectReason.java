package com.tlong.center.domain.web;

import com.tlong.center.api.dto.web.WebGoodsReasonResponseDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_goods_reject_reason")
@DynamicUpdate
public class WebGoodsRejectReason extends BaseJpa {

    public WebGoodsRejectReason() {

    }

    public WebGoodsRejectReason(WebGoodsReasonResponseDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebGoodsReasonResponseDto toDto() {
        WebGoodsReasonResponseDto dto = new WebGoodsReasonResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品id
    private Long goodsId;

    //商品名称
    private String goodsHead;

    //商品分类
    private String goodsClass;

    //商品描述
    private String des;


    //商品属性
    private String goodsProperty;

    //商品视频
    private String video;

    //商品证书
    private String certificate;

    //商品图片地址
    private String goodsPic;

    //图片类型
    private String picType;

    //圈口大小
    private String circle;

    //题材
    private String theme;

    //款式
    private String style;

    //种水
    private String kindOfWater;

    //颜色
    private String color;

    //发布价
    private String publishPrice;

    //数量
    private String num;

    //价格类型
    private String priceType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
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

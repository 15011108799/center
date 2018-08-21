package com.tlong.center.api.dto.app.goods;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP商品上传请求数据模型")
public class AppGoodsUploadRequestDto implements Serializable {

    @ApiModelProperty("文件类别")
    private String contentClass;

    @ApiModelProperty("商品名称")
    private String goodsHead;

    @ApiModelProperty("商品编码")
    private String goodsCode;

    @ApiModelProperty("商品属性id")
    private Long goodsPropertyId;

    @ApiModelProperty("商品图片URL")
    private String goodsPic;

    @ApiModelProperty("商品发布人")
    private String publishUserId;

    @ApiModelProperty("商品分类")
    private String goodsClassId;

    @ApiModelProperty("商品分类父id")
    private Long parentClassId;

    @ApiModelProperty("商品描述")
    private String des;

    @ApiModelProperty("商品详情")
    private String detail;

    @ApiModelProperty("商品星级")
    private String star;

    @ApiModelProperty("上传类别")
    private String publishClass;

    @ApiModelProperty("商品视频")
    private String video;

    @ApiModelProperty("商品证书")
    private String certificate;

    @ApiModelProperty("图片类型")
    private String picType;

    @ApiModelProperty("圈口大小")
    private String circle;

    @ApiModelProperty("题材")
    private String theme;

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

    @ApiModelProperty("发布时间")
    private String publishTime;

    @ApiModelProperty("发布人姓名")
    private String publishName;

    @ApiModelProperty("发布人编码")
    private String publishCode;

    @ApiModelProperty("发布人电话")
    private String publishPhone;

    @ApiModelProperty("是否审核")
    private String isCheck;

    @ApiModelProperty("发布人编码")
    private String userCode;

    @ApiModelProperty("发布人是否是公司")
    private Integer isCompany;

    public String getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(String publishCode) {
        this.publishCode = publishCode;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getContentClass() {
        return contentClass;
    }

    public void setContentClass(String contentClass) {
        this.contentClass = contentClass;
    }

    public String getGoodsHead() {
        return goodsHead;
    }

    public void setGoodsHead(String goodsHead) {
        this.goodsHead = goodsHead;
    }

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public String getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(String publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(String goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public Long getParentClassId() {
        return parentClassId;
    }

    public void setParentClassId(Long parentClassId) {
        this.parentClassId = parentClassId;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getPublishClass() {
        return publishClass;
    }

    public void setPublishClass(String publishClass) {
        this.publishClass = publishClass;
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


    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getPublishPhone() {
        return publishPhone;
    }

    public void setPublishPhone(String publishPhone) {
        this.publishPhone = publishPhone;
    }

    public String getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(String isCheck) {
        this.isCheck = isCheck;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public Long getGoodsPropertyId() {
        return goodsPropertyId;
    }

    public void setGoodsPropertyId(Long goodsPropertyId) {
        this.goodsPropertyId = goodsPropertyId;
    }
}

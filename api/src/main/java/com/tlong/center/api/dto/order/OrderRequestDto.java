package com.tlong.center.api.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("订单请求模型")
public class OrderRequestDto implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String realName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户编码")
    private String userCode;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品图片")
    private String goodsUrl;

    @ApiModelProperty("商品发布人姓名")
    private String publishName;

    @ApiModelProperty("商品发布人编码")
    private String publishCode;

    @ApiModelProperty("商品发布人电话")
    private String publishPhone;

    @ApiModelProperty("商品编号")
    private String goodsCode;

    @ApiModelProperty("商品评星")
    private String goodsStar;

    @ApiModelProperty("下单人成交价格")
    private Double goodsPrice;

    @ApiModelProperty("发布价格")
    private Double publishPrice;

    @ApiModelProperty("创始人旗舰店价格")
    private Double founderPrice;

    @ApiModelProperty("订单状态")
    private Integer state;

    @ApiModelProperty("下单时间")
    private String placeOrderTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getGoodsUrl() {
        return goodsUrl;
    }

    public void setGoodsUrl(String goodsUrl) {
        this.goodsUrl = goodsUrl;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsStar() {
        return goodsStar;
    }

    public void setGoodsStar(String goodsStar) {
        this.goodsStar = goodsStar;
    }

    public Double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(Double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPlaceOrderTime() {
        return placeOrderTime;
    }

    public void setPlaceOrderTime(String placeOrderTime) {
        this.placeOrderTime = placeOrderTime;
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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Double getFounderPrice() {
        return founderPrice;
    }

    public void setFounderPrice(Double founderPrice) {
        this.founderPrice = founderPrice;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(String publishCode) {
        this.publishCode = publishCode;
    }

    public Double getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(Double publishPrice) {
        this.publishPrice = publishPrice;
    }
}

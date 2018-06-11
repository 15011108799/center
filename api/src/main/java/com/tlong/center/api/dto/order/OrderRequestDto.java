package com.tlong.center.api.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("订单请求模型")
public class OrderRequestDto implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户手机号")
    private String userPhone;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("商品图片")
    private String goodsUrl;

    @ApiModelProperty("商品编号")
    private String goodsCode;

    @ApiModelProperty("商品评星")
    private String goodsStar;

    @ApiModelProperty("商品价格")
    private Double goodsPrice;

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
}

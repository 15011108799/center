package com.tlong.center.domain.web;

import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_order")
@DynamicUpdate
public class WebOrder {
    public WebOrder() {

    }

    public WebOrder(OrderRequestDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public OrderRequestDto toDto() {
        OrderRequestDto dto = new OrderRequestDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //用户id
    private Long userId;

    //商品id
    private Long goodsId;

    //订单状态
    private Integer state;

    //下单时间
    private String placeOrderTime;

//    //下单人用户名
//    private String userName;
//
//    //下单人编码
//    private String userCode;
//
//    //商品名称
//    private String goodsName;
//
//    //商品编码
//    private String goodsCode;
//
//    //发布人id
//    private Long publishUserId;
//
//    //发布人编码
//    private String publishUserCode;
//
//    //发布人手机号
//    private String publishUserPhone;
//
//    //下单人手机号
//    private String phone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
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

//    public String getUserName() {
//        return userName;
//    }
//
//    public void setUserName(String userName) {
//        this.userName = userName;
//    }
//
//    public String getUserCode() {
//        return userCode;
//    }
//
//    public void setUserCode(String userCode) {
//        this.userCode = userCode;
//    }
//
//    public String getGoodsName() {
//        return goodsName;
//    }
//
//    public void setGoodsName(String goodsName) {
//        this.goodsName = goodsName;
//    }
//
//    public String getGoodsCode() {
//        return goodsCode;
//    }
//
//    public void setGoodsCode(String goodsCode) {
//        this.goodsCode = goodsCode;
//    }
//
//    public Long getPublishUserId() {
//        return publishUserId;
//    }
//
//    public void setPublishUserId(Long publishUserId) {
//        this.publishUserId = publishUserId;
//    }
//
//    public String getPublishUserCode() {
//        return publishUserCode;
//    }
//
//    public void setPublishUserCode(String publishUserCode) {
//        this.publishUserCode = publishUserCode;
//    }
//
//    public String getPublishUserPhone() {
//        return publishUserPhone;
//    }
//
//    public void setPublishUserPhone(String publishUserPhone) {
//        this.publishUserPhone = publishUserPhone;
//    }
//
//    public String getPhone() {
//        return phone;
//    }
//
//    public void setPhone(String phone) {
//        this.phone = phone;
//    }
}

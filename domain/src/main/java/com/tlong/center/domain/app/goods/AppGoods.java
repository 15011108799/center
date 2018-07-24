//package com.tlong.center.domain.app.goods;
//
//import com.tlong.core.base.BaseJpa;
//import org.hibernate.annotations.DynamicUpdate;
//
//import javax.persistence.*;
//
//@Entity
//@Table(name = "tlong_goods")
//@DynamicUpdate
//public class AppGoods extends BaseJpa {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
//
//    //商品名称
//    private String goodsName;
//
//    //商品编码
//    private String goodsCode;
//
//    //商品出厂价格
//    private Double factoryPrice;
//
//    //商品图片地址
//    private String picUrls;
//
//    //当前状态(1启用 0禁用)
//    private Integer curState = 1;
//
//    //是否已删除(1已删除 0未删除)
//    private Integer isDeleted = 0;
//
//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
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
//    public Double getFactoryPrice() {
//        return factoryPrice;
//    }
//
//    public void setFactoryPrice(Double factoryPrice) {
//        this.factoryPrice = factoryPrice;
//    }
//
//    public String getPicUrls() {
//        return picUrls;
//    }
//
//    public void setPicUrls(String picUrls) {
//        this.picUrls = picUrls;
//    }
//
//    public Integer getCurState() {
//        return curState;
//    }
//
//    public void setCurState(Integer curState) {
//        this.curState = curState;
//    }
//
//    public Integer getIsDeleted() {
//        return isDeleted;
//    }
//
//    public void setIsDeleted(Integer isDeleted) {
//        this.isDeleted = isDeleted;
//    }
//}

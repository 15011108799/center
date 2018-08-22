package com.tlong.center.domain.app.goods;

import com.tlong.center.api.dto.app.goods.AppGoodsUploadRequestDto;
import com.tlong.center.api.dto.app.goods.AppIndexGoodsRequestDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_goods")
@DynamicUpdate
public class WebGoods extends BaseJpa {

    public WebGoods() {

    }
    public WebGoods(WebGoodsDetailResponseDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebGoods(AppGoodsUploadRequestDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebGoodsDetailResponseDto toDto() {
        WebGoodsDetailResponseDto dto = new WebGoodsDetailResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品名称
    private String goodsHead;

    //商品编码
    private String goodsCode;

    //商品发布人
    private Long publishUserId;

    //商品分类
    private Long goodsClassId;

    //当前状态
    private Integer state;

    //商品描述
    private String des;

    //商品详情
    private String detail;

    //商品属性id
    private Long goodsPropertyId;

    //商品星级
    private String star;

    //商品真实评星值
    private Integer realStar;

    //上传类别
    private String publishClass;

    //商品视频
    private String video;

    //商品证书
    private String certificate;

    //商品图片地址
    private String goodsPic;

    //图片类型
    private Integer picType;

    //圈口大小
    private Integer circle;

    //题材
    private String theme;

    //款式
    private String style;

    //种水
    private String kindOfWater;

    //颜色
    private String color;

    //商品出厂价格
    private Double factoryPrice;

    //创始人价格
    private Double founderPrice;

    //旗舰店价格
    private Double flagshipPrice;

    //专卖店价格
    private Double storePrice;

    //发布价
    private Double publishPrice;

    //价格区间
    private String priceRange;

    //数量
    private Integer num;

    //价格类型
    private Integer priceType;

    //微信客服
    private String wx;

    //服务电话
    private String phoneService;

    //发布时间
    private String publishTime;

    //是否审核
    private Integer isCheck;

    //当前状态
    private Integer curState;

    //是否删除
    private Integer isDeleted;

    public Integer getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(Integer isCheck) {
        this.isCheck = isCheck;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsHead() {
        return goodsHead;
    }

    public void setGoodsHead(String goodsHead) {
        this.goodsHead = goodsHead;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Long getPublishUserId() {
        return publishUserId;
    }

    public void setPublishUserId(Long publishUserId) {
        this.publishUserId = publishUserId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public Long getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Long goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Integer getRealStar() {
        return realStar;
    }

    public void setRealStar(Integer realStar) {
        this.realStar = realStar;
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

    public String getGoodsPic() {
        return goodsPic;
    }

    public void setGoodsPic(String goodsPic) {
        this.goodsPic = goodsPic;
    }

    public Integer getPicType() {
        return picType;
    }

    public void setPicType(Integer picType) {
        this.picType = picType;
    }

    public Integer getCircle() {
        return circle;
    }

    public void setCircle(Integer circle) {
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

    public Double getFactoryPrice() {
        return factoryPrice;
    }

    public void setFactoryPrice(Double factoryPrice) {
        this.factoryPrice = factoryPrice;
    }

    public Double getFounderPrice() {
        return founderPrice;
    }

    public void setFounderPrice(Double founderPrice) {
        this.founderPrice = founderPrice;
    }

    public Double getFlagshipPrice() {
        return flagshipPrice;
    }

    public void setFlagshipPrice(Double flagshipPrice) {
        this.flagshipPrice = flagshipPrice;
    }

    public Double getStorePrice() {
        return storePrice;
    }

    public Double getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(Double publishPrice) {
        this.publishPrice = publishPrice;
    }

    public void setStorePrice(Double storePrice) {
        this.storePrice = storePrice;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPriceType() {
        return priceType;
    }

    public void setPriceType(Integer priceType) {
        this.priceType = priceType;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

    public String getPhoneService() {
        return phoneService;
    }

    public void setPhoneService(String phoneService) {
        this.phoneService = phoneService;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getCurState() {

        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }

    public Long getGoodsPropertyId() {
        return goodsPropertyId;
    }

    public void setGoodsPropertyId(Long goodsPropertyId) {
        this.goodsPropertyId = goodsPropertyId;
    }
}

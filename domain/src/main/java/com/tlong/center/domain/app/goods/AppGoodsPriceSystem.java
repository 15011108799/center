package com.tlong.center.domain.app.goods;


import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_goods_price_system")
@DynamicUpdate
public class AppGoodsPriceSystem {

    public AppGoodsPriceSystem() {

    }

    public AppGoodsPriceSystem(WebGoodsClassRequestDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebGoodsClassRequestDto toDto() {
        WebGoodsClassRequestDto dto = new WebGoodsClassRequestDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品分类id
    private Long goodsClassId;

    //价格区间上限
    private Double intervalUp;

    //价格区间下限
    private Double intervalDown;

    //价格倍率
    private Double radio;

    //创始人倍率
    private Double originatorRatio;

    //旗舰店倍率
    private Double lagshipRatio;

    //专卖店倍率
    private Double storeRatio;

    //出厂倍率
    private Double factoryRatio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Long goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public Double getIntervalUp() {
        return intervalUp;
    }

    public void setIntervalUp(Double intervalUp) {
        this.intervalUp = intervalUp;
    }

    public Double getIntervalDown() {
        return intervalDown;
    }

    public void setIntervalDown(Double intervalDown) {
        this.intervalDown = intervalDown;
    }

    public Double getOriginatorRatio() {
        return originatorRatio;
    }

    public void setOriginatorRatio(Double originatorRatio) {
        this.originatorRatio = originatorRatio;
    }

    public Double getLagshipRatio() {
        return lagshipRatio;
    }

    public void setLagshipRatio(Double lagshipRatio) {
        this.lagshipRatio = lagshipRatio;
    }

    public Double getStoreRatio() {
        return storeRatio;
    }

    public void setStoreRatio(Double storeRatio) {
        this.storeRatio = storeRatio;
    }

    public Double getFactoryRatio() {
        return factoryRatio;
    }

    public void setFactoryRatio(Double factoryRatio) {
        this.factoryRatio = factoryRatio;
    }

    public Double getRadio() {
        return radio;
    }

    public void setRadio(Double radio) {
        this.radio = radio;
    }
}

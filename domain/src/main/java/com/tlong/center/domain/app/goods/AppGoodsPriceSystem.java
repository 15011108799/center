package com.tlong.center.domain.app.goods;


import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_goods_price_system")
@DynamicUpdate
public class AppGoodsPriceSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品分类id
    private Long goodsClassId;

    //价格区间上限
    private Double intervalUp;

    //价格区间下限
    private Double intervalDown;

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
}

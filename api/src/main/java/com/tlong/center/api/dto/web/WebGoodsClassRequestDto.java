package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("商品分类数据模型")
public class WebGoodsClassRequestDto implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品名称")
    private String className;

    @ApiModelProperty("分类层级")
    private Integer classLevel;

    @ApiModelProperty("父分类id")
    private Long parentClassId;

    @ApiModelProperty("价格区间上限")
    private Double intervalUp;

    @ApiModelProperty("价格区间下限")
    private Double intervalDown;

    @ApiModelProperty("创始人倍率")
    private Double originatorRatio;

    @ApiModelProperty("旗舰店倍率")
    private Double lagshipRatio;

    @ApiModelProperty("专卖店倍率")
    private Double storeRatio;

    @ApiModelProperty("出厂倍率")
    private Double factoryRatio;

    @ApiModelProperty("发布时间")
    private String publishTime;

    public WebGoodsClassRequestDto(Long id, String className, Integer classLevel, Long parentClassId, Double originatorRatio, Double lagshipRatio, Double storeRatio, Double factoryRatio, String publishTime, Double intervalUp,Double intervalDown) {
        this.id = id;
        this.className = className;
        this.classLevel = classLevel;
        this.parentClassId = parentClassId;
        this.originatorRatio = originatorRatio;
        this.lagshipRatio = lagshipRatio;
        this.storeRatio = storeRatio;
        this.factoryRatio = factoryRatio;
        this.publishTime = publishTime;
        this.intervalUp=intervalUp;
        this.intervalDown=intervalDown;
    }

    public WebGoodsClassRequestDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Integer classLevel) {
        this.classLevel = classLevel;
    }

    public Long getParentClassId() {
        return parentClassId;
    }

    public void setParentClassId(Long parentClassId) {
        this.parentClassId = parentClassId;
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

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
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
}

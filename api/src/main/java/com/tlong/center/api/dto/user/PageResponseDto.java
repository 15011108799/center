package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;
@ApiModel("分页供应商模型")
public class PageResponseDto<T> implements Serializable{
    private List<T> list;
    private Integer count;
    private Integer orderNum;
    private Double publishPrice;
    private Double founderPrice;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Double getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(Double publishPrice) {
        this.publishPrice = publishPrice;
    }

    public Double getFounderPrice() {
        return founderPrice;
    }

    public void setFounderPrice(Double founderPrice) {
        this.founderPrice = founderPrice;
    }
}

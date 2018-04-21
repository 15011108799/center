package com.tlong.center.api.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("分页排序请求模型")
public class
PageAndSortRequestDto implements Serializable{
    private static final long serialVersionUID = -6097290648750753950L;

    @ApiModelProperty("起始页")
    private Integer page;

    @ApiModelProperty("每页数量")
    private Integer size;

    @ApiModelProperty("排序依据属性")
    private String orderProperty;

    @ApiModelProperty("顺序规则 1正序 0倒序")
    private Integer direction;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getOrderProperty() {
        return orderProperty;
    }

    public void setOrderProperty(String orderProperty) {
        this.orderProperty = orderProperty;
    }

    public Integer getDirection() {
        return direction;
    }

    public void setDirection(Integer direction) {
        this.direction = direction;
    }
}

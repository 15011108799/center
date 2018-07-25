package com.tlong.center.api.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("分页排序请求模型")
public class PageAndSortRequestDto implements Serializable{
    private static final long serialVersionUID = -6097290648750753950L;

    @ApiModelProperty("起始页")
    private Integer page;

    @ApiModelProperty("类型")
    private Integer type;

    @ApiModelProperty("父id")
    private Long pid;

    @ApiModelProperty("等级")
    private Long Level;

    @ApiModelProperty("组织")
    private String org;

    @ApiModelProperty("是否展示当月")
    private Integer currentMonth;

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

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getLevel() {
        return Level;
    }

    public void setLevel(Long level) {
        Level = level;
    }

    public String getOrg() {
        return org;
    }

    public void setOrg(String org) {
        this.org = org;
    }

    public Integer getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(Integer currentMonth) {
        this.currentMonth = currentMonth;
    }

}

package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("天珑权限数据模型")
public class TlongPowerDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("权限名称")
    private String powerName;

    @ApiModelProperty("权限级别(0左侧一级菜单 1左侧二级菜单 2右侧细分功能)")
    private Integer powerLevel;

    public TlongPowerDto() {
    }

    public TlongPowerDto(Long id, String powerName, Integer powerLevel) {
        this.id = id;
        this.powerName = powerName;
        this.powerLevel = powerLevel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public Integer getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }
}

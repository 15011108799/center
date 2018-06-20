package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("天珑权限数据模型")
public class TlongPowerDto implements Serializable {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("权限名称")
    private String powerName;

    @ApiModelProperty("权限级别(0左侧一级菜单 1左侧二级菜单 2右侧细分功能)")
    private Integer powerLevel;

    @ApiModelProperty("权限父id")
    private Integer pid;

    @ApiModelProperty("url")
    private String url;

    @ApiModelProperty("三级权限名称")
    private List<String> threeLevel=new ArrayList<>();

    public TlongPowerDto() {
    }

    public TlongPowerDto(Long id, String powerName, Integer powerLevel,Integer pid,String url) {
        this.id = id;
        this.powerName = powerName;
        this.powerLevel = powerLevel;
        this.pid=pid;
        this.url=url;
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

    public Integer getPid() {
        return pid;
    }

    public void setPid(Integer pid) {
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getThreeLevel() {
        return threeLevel;
    }

    public void setThreeLevel(List<String> threeLevel) {
        this.threeLevel = threeLevel;
    }
}

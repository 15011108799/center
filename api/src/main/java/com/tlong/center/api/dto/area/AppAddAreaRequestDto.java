package com.tlong.center.api.dto.area;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("app区域新增请求模型")
public class AppAddAreaRequestDto implements Serializable{
    private static final long serialVersionUID = -3586372113379926926L;

    @ApiModelProperty("地域名称")
    private String areaName;

    @ApiModelProperty("地域级别(1国家 2大区 3省 4市 5区丶县)")
    private Integer areaLevel;

    @ApiModelProperty("父级地域id")
    private Long parentId;

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getAreaLevel() {
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel) {
        this.areaLevel = areaLevel;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
}

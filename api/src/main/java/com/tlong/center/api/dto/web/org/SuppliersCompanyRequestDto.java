package com.tlong.center.api.dto.web.org;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("代理商机构查询请求数据模型")
public class SuppliersCompanyRequestDto extends PageAndSortRequestDto implements Serializable {

    @ApiModelProperty("机构类型 0总公司管理员 1省级代理商管理员 2市级代理商管理员 3县区级管理员 4供应商分公司管理员")
    private Integer orgClass;

    @ApiModelProperty("用户id")
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getOrgClass() {
        return orgClass;
    }

    public void setOrgClass(Integer orgClass) {
        this.orgClass = orgClass;
    }
}

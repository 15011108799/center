package com.tlong.center.api.dto.web.department;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("部门信息返回数据模型")
public class DepartmentInfoResponseDto implements Serializable {

    @ApiModelProperty("部门名称")
    private String departmentName;

    @ApiModelProperty("所属机构")
    private String orgName;

    @ApiModelProperty("部门成立时间")
    private String registDate;

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }
}

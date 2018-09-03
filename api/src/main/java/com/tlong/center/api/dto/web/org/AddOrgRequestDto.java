package com.tlong.center.api.dto.web.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("新增机构数据模型")
public class AddOrgRequestDto implements Serializable {

    @ApiModelProperty("父级机构id")
    private Long parentOrgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("部门描述")
    private String orgDesc;

    @ApiModelProperty("机构邮箱")
    private String orgEmail;

    @ApiModelProperty("机构电话")
    private String orgPhone;

    @ApiModelProperty("机构类型  0总公司管理员 1省级代理商管理员 2市级代理商管理员 3县区级管理员 4供应商分公司管理员")
    private Integer orgClass;

    @ApiModelProperty("机构级别")
    private Integer orgLevel;

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public Integer getOrgClass() {
        return orgClass;
    }

    public void setOrgClass(Integer orgClass) {
        this.orgClass = orgClass;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    public String getOrgEmail() {
        return orgEmail;
    }

    public void setOrgEmail(String orgEmail) {
        this.orgEmail = orgEmail;
    }

    public String getOrgPhone() {
        return orgPhone;
    }

    public void setOrgPhone(String orgPhone) {
        this.orgPhone = orgPhone;
    }
}

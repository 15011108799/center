package com.tlong.center.api.dto.web.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("代理商机构查询返回数据模型")
public class SuppliersCompanyResponseDto implements Serializable {

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("所属机构名称")
    private String belongToOrgName;

    @ApiModelProperty("身份")
    private Integer orgClass;

    @ApiModelProperty("所属大区")
    private String userArea;

    @ApiModelProperty("注册时间")
    private String registDate;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getBelongToOrgName() {
        return belongToOrgName;
    }

    public void setBelongToOrgName(String belongToOrgName) {
        this.belongToOrgName = belongToOrgName;
    }

    public Integer getOrgClass() {
        return orgClass;
    }

    public void setOrgClass(Integer orgClass) {
        this.orgClass = orgClass;
    }

    public String getUserArea() {
        return userArea;
    }

    public void setUserArea(String userArea) {
        this.userArea = userArea;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }
}

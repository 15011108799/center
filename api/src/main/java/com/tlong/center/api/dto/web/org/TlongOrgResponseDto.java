package com.tlong.center.api.dto.web.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("代理商机构返回模型")
public class TlongOrgResponseDto implements Serializable {

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("所属机构id (父级机构id)")
    private Long parentOrgId;

    @ApiModelProperty("所属机构名称")
    private String parentOrgName;

    @ApiModelProperty("身份级别 1总公司 2代理商省级分公司 3代理商市级分公司")
    private Integer orgLevel;

    @ApiModelProperty("注册时间")
    private Date createDate;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public Long getParentOrgId() {
        return parentOrgId;
    }

    public void setParentOrgId(Long parentOrgId) {
        this.parentOrgId = parentOrgId;
    }

    public String getParentOrgName() {
        return parentOrgName;
    }

    public void setParentOrgName(String parentOrgName) {
        this.parentOrgName = parentOrgName;
    }

    public Integer getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(Integer orgLevel) {
        this.orgLevel = orgLevel;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}

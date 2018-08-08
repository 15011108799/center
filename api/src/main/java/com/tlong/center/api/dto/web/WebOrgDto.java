package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("机构数据模型")
public class WebOrgDto implements Serializable{

    @ApiModelProperty("机构id")
    private Long id;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("创建时间")
    private LocalDateTime createDate;

    @ApiModelProperty("机构地址")
    private String orgAddress;

    @ApiModelProperty("机构描述")
    private String orgDesc;

    @ApiModelProperty("机构邮箱")
    private String orgEmail;

    @ApiModelProperty("机构电话")
    private String orgPhone;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
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

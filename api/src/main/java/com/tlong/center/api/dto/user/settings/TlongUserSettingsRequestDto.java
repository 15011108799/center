package com.tlong.center.api.dto.user.settings;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("设置用户发布商品数量")
public class TlongUserSettingsRequestDto implements Serializable {

    //用户类型
//    private Integer userType;

    //个人发布数量
    private Integer personReleaseNumber;

    //企业发布数量
    private Integer CompanyReleaseNumber;

    //个人重新发布数量
    private Integer personReReleaseNumber;

    //企业重新发布数量
    private Integer CompanyReReleaseNumber;

    //个人编码规则前缀
    private String personHeadContent;

    //企业编码规则前缀
    private String companyHeadContent;

    public Integer getPersonReleaseNumber() {
        return personReleaseNumber;
    }

    public void setPersonReleaseNumber(Integer personReleaseNumber) {
        this.personReleaseNumber = personReleaseNumber;
    }

    public Integer getCompanyReleaseNumber() {
        return CompanyReleaseNumber;
    }

    public void setCompanyReleaseNumber(Integer companyReleaseNumber) {
        CompanyReleaseNumber = companyReleaseNumber;
    }

    public Integer getPersonReReleaseNumber() {
        return personReReleaseNumber;
    }

    public void setPersonReReleaseNumber(Integer personReReleaseNumber) {
        this.personReReleaseNumber = personReReleaseNumber;
    }

    public Integer getCompanyReReleaseNumber() {
        return CompanyReReleaseNumber;
    }

    public void setCompanyReReleaseNumber(Integer companyReReleaseNumber) {
        CompanyReReleaseNumber = companyReReleaseNumber;
    }

    public String getPersonHeadContent() {
        return personHeadContent;
    }

    public void setPersonHeadContent(String personHeadContent) {
        this.personHeadContent = personHeadContent;
    }

    public String getCompanyHeadContent() {
        return companyHeadContent;
    }

    public void setCompanyHeadContent(String companyHeadContent) {
        this.companyHeadContent = companyHeadContent;
    }
}

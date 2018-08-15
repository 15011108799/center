package com.tlong.center.api.dto.user.settings;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel("用户发布商品数量数据模型")
public class TlongUserSettingsRequestDto implements Serializable {

    //用户id
    private Long userId;

    //用户类型
    private Integer userType;

    //个人发布数量
    private Integer personReleaseNumber;

    //企业发布数量
    private Integer companyReleaseNumber;

    //个人重新发布数量
    private Integer personReReleaseNumber;

    //企业重新发布数量
    private Integer companyReReleaseNumber;

    //个人编码规则前缀
    private String personHeadContent;

    //企业编码规则前缀
    private String companyHeadContent;

    private String personBehind;

    private String companyBehind;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getPersonBehind() {
        return personBehind;
    }

    public void setPersonBehind(String personBehind) {
        this.personBehind = personBehind;
    }

    public String getCompanyBehind() {
        return companyBehind;
    }

    public void setCompanyBehind(String companyBehind) {
        this.companyBehind = companyBehind;
    }

    public Integer getPersonReleaseNumber() {
        return personReleaseNumber;
    }

    public void setPersonReleaseNumber(Integer personReleaseNumber) {
        this.personReleaseNumber = personReleaseNumber;
    }

    public Integer getPersonReReleaseNumber() {
        return personReReleaseNumber;
    }

    public void setPersonReReleaseNumber(Integer personReReleaseNumber) {
        this.personReReleaseNumber = personReReleaseNumber;
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

    public Integer getCompanyReleaseNumber() {
        return companyReleaseNumber;
    }

    public void setCompanyReleaseNumber(Integer companyReleaseNumber) {
        this.companyReleaseNumber = companyReleaseNumber;
    }

    public Integer getCompanyReReleaseNumber() {
        return companyReReleaseNumber;
    }

    public void setCompanyReReleaseNumber(Integer companyReReleaseNumber) {
        this.companyReReleaseNumber = companyReReleaseNumber;
    }
}

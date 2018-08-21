package com.tlong.center.api.dto.app.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP登录返回模型")
public class AppUserLoginResponseDto implements Serializable{
    private static final long serialVersionUID = 6836750200118874212L;

    @ApiModelProperty("处理结果")
    private Integer flag;

    @ApiModelProperty("用户Id")
    private Long UserId;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("用户类型 1代理商 2供应商")
    private Integer userType;

    @ApiModelProperty("E签宝认证状态(1已认证 0未认证)")
    private Integer esgin;

    @ApiModelProperty("审核状态 1已审核 0未审核")
    private Integer isChecked;

    @ApiModelProperty("是否是企业")
    private Integer isCompany;


    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Long getUserId() {
        return UserId;
    }

    public void setUserId(Long userId) {
        UserId = userId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getEsgin() {
        return esgin;
    }

    public void setEsgin(Integer esgin) {
        this.esgin = esgin;
    }

    public Integer getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(Integer isChecked) {
        this.isChecked = isChecked;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }
}

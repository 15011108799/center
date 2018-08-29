package com.tlong.center.api.dto.app.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("APP用户注册数据模型")
public class AppUserRequestDto implements Serializable {

    @ApiModelProperty("父级id")
    private Long parentId;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号码")
    private String phone;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

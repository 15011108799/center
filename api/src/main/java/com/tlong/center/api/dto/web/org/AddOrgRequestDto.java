package com.tlong.center.api.dto.web.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("新增机构数据模型")
public class AddOrgRequestDto implements Serializable {

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("部门描述")
    private String orgDesc;

    @ApiModelProperty("机构邮箱")
    private String orgEmail;

    @ApiModelProperty("机构电话")
    private String orgPhone;

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

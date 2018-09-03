package com.tlong.center.api.dto.web.department;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("新增部门请求数据模型")
public class AddDepartmentRequestDto implements Serializable {

    @ApiModelProperty("部门名称(角色名称)")
    private String roleName;

    @ApiModelProperty("部门描述")
    private String roleDesc;

    @ApiModelProperty("类型")
    private Integer type;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }
}

package com.tlong.center.api.dto.web.org;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("修改机构请求接口")
public class UpdateOrgRequestDto extends AddOrgRequestDto{

    @ApiModelProperty("机构id")
    private Long orgId;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }
}

package com.tlong.center.api.dto.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("商品分页查询数据模型")
public class WebGoodsPageRequestDto extends PageAndSortRequestDto{

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("是否是集团")
    private Integer isCompany;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

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

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }
}

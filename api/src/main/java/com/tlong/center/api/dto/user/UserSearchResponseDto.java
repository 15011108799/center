package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ApiModel("用户搜索请求模型")
public class UserSearchResponseDto implements Serializable {
    private static final long serialVersionUID = 6324694164911170342L;

    @ApiModelProperty("用户数据")
    private List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos=new ArrayList<>();

    public List<SuppliersRegisterRequsetDto> getSuppliersRegisterRequsetDtos() {
        return suppliersRegisterRequsetDtos;
    }

    public void setSuppliersRegisterRequsetDtos(List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos) {
        this.suppliersRegisterRequsetDtos = suppliersRegisterRequsetDtos;
    }
}

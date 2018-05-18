package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("代理商注册请求模型")
public class SuppliersRegisterRequsetDto implements Serializable{
    private static final long serialVersionUID = -7697313937998312834L;

    @ApiModelProperty("供应商id")
    private Long id;

    @ApiModelProperty("用户名(标题)")
    private String realName;

    @ApiModelProperty("用户编码")
    private String userCode;

}

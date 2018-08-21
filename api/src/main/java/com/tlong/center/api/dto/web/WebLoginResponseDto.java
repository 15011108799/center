package com.tlong.center.api.dto.web;

import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;

import java.io.Serializable;
import java.util.List;

@ApiModel("后台登录返回数据模型")
public class WebLoginResponseDto extends TlongResultDto implements Serializable{

    @ApiModelProperty("一级权限列表")
    private List<TlongPowerDto> powersLevelOne;

    @ApiModelProperty("二级权限列表")
    private List<TlongPowerDto> powersLevelTwo;

  /*  @ApiModelProperty("三级权限列表")
    private List<String> powersLevelThree;*/

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("集团id")
    private Long orgId;

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

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

    public WebLoginResponseDto() {
    }

    public WebLoginResponseDto(Integer result, String content) {
        super(result,content);
    }

    public List<TlongPowerDto> getPowersLevelOne() {
        return powersLevelOne;
    }

    public void setPowersLevelOne(List<TlongPowerDto> powersLevelOne) {
        this.powersLevelOne = powersLevelOne;
    }

    public List<TlongPowerDto> getPowersLevelTwo() {
        return powersLevelTwo;
    }

    public void setPowersLevelTwo(List<TlongPowerDto> powersLevelTwo) {
        this.powersLevelTwo = powersLevelTwo;
    }

  /*  public List<String> getPowersLevelThree() {
        return powersLevelThree;
    }

    public void setPowersLevelThree(List<String> powersLevelThree) {
        this.powersLevelThree = powersLevelThree;
    }*/

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

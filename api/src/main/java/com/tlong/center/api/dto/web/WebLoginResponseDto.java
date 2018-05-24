package com.tlong.center.api.dto.web;

import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("后台登录返回数据模型")
public class WebLoginResponseDto extends TlongResultDto implements Serializable{

    @ApiModelProperty("一级权限列表")
    private List<TlongPowerDto> powersLevelOne;

    @ApiModelProperty("二级权限列表")
    private List<TlongPowerDto> powersLevelTwo;

    @ApiModelProperty("三级权限列表")
    private List<TlongPowerDto> powersLevelThree;


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

    public List<TlongPowerDto> getPowersLevelThree() {
        return powersLevelThree;
    }

    public void setPowersLevelThree(List<TlongPowerDto> powersLevelThree) {
        this.powersLevelThree = powersLevelThree;
    }
}

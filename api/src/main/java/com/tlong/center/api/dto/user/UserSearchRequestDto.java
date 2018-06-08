package com.tlong.center.api.dto.user;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.time.LocalDateTime;

@ApiModel("用户查询信息返回模型")
public class UserSearchRequestDto implements Serializable {
    private static final long serialVersionUID = -6773010187440278171L;

    @ApiModelProperty("分页排序")
    private PageAndSortRequestDto pageAndSortRequestDto;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("e签宝认证状态")
    private Integer esign;

    @ApiModelProperty("后台认证状态")
    private Integer authentication;

    @ApiModelProperty("身份类型")
    private Integer ptype;

    public PageAndSortRequestDto getPageAndSortRequestDto() {
        return pageAndSortRequestDto;
    }

    public void setPageAndSortRequestDto(PageAndSortRequestDto pageAndSortRequestDto) {
        this.pageAndSortRequestDto = pageAndSortRequestDto;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public Integer getEsign() {
        return esign;
    }

    public void setEsign(Integer esign) {
        this.esign = esign;
    }

    public Integer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Integer authentication) {
        this.authentication = authentication;
    }

    public Integer getPtype() {
        return ptype;
    }

    public void setPtype(Integer ptype) {
        this.ptype = ptype;
    }
}

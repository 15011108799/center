package com.tlong.center.api.dto.goods;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("用户查询信息返回模型")
public class GoodsSearchRequestDto implements Serializable {

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("机构id")
    private Long orgId;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("是否是企业")
    private Integer isCompany;

    @ApiModelProperty("分页排序")
    private PageAndSortRequestDto pageAndSortRequestDto;

    @ApiModelProperty("商品状态")
    private Integer goodsState;

    @ApiModelProperty("审核状态")
    private Integer checkState;

    @ApiModelProperty("商品发布人")
    private String publishName;

    @ApiModelProperty("商品编码")
    private String goodsCode;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public PageAndSortRequestDto getPageAndSortRequestDto() {
        return pageAndSortRequestDto;
    }

    public void setPageAndSortRequestDto(PageAndSortRequestDto pageAndSortRequestDto) {
        this.pageAndSortRequestDto = pageAndSortRequestDto;
    }

    public Integer getGoodsState() {
        return goodsState;
    }

    public void setGoodsState(Integer goodsState) {
        this.goodsState = goodsState;
    }

    public Integer getCheckState() {
        return checkState;
    }

    public void setCheckState(Integer checkState) {
        this.checkState = checkState;
    }

    public String getPublishName() {
        return publishName;
    }

    public void setPublishName(String publishName) {
        this.publishName = publishName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}

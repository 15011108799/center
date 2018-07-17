package com.tlong.center.api.dto.order;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("分类条件查询信息请求模型")
public class OrderSearchRequestDto implements Serializable {

    @ApiModelProperty("分页排序")
    private PageAndSortRequestDto pageAndSortRequestDto;

    @ApiModelProperty("订单状态")
    private Integer orderState;

    @ApiModelProperty("产品编号")
    private String goodsCode;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("下单人编号")
    private String placeUsesrCode;

    @ApiModelProperty("下单人用户名")
    private String placeUserName;

    @ApiModelProperty("发布人编号")
    private String publishCode;

    public PageAndSortRequestDto getPageAndSortRequestDto() {
        return pageAndSortRequestDto;
    }

    public void setPageAndSortRequestDto(PageAndSortRequestDto pageAndSortRequestDto) {
        this.pageAndSortRequestDto = pageAndSortRequestDto;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
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

    public String getPlaceUsesrCode() {
        return placeUsesrCode;
    }

    public void setPlaceUsesrCode(String placeUsesrCode) {
        this.placeUsesrCode = placeUsesrCode;
    }

    public String getPlaceUserName() {
        return placeUserName;
    }

    public void setPlaceUserName(String placeUserName) {
        this.placeUserName = placeUserName;
    }

    public String getPublishCode() {
        return publishCode;
    }

    public void setPublishCode(String publishCode) {
        this.publishCode = publishCode;
    }
}

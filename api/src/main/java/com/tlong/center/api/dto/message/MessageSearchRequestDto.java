package com.tlong.center.api.dto.message;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("分类条件查询信息请求模型")
public class MessageSearchRequestDto implements Serializable {

    @ApiModelProperty("分页排序")
    private PageAndSortRequestDto pageAndSortRequestDto;

    @ApiModelProperty("审核状态")
    private Integer state;

    @ApiModelProperty("信息标题")
    private String title;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public PageAndSortRequestDto getPageAndSortRequestDto() {
        return pageAndSortRequestDto;
    }

    public void setPageAndSortRequestDto(PageAndSortRequestDto pageAndSortRequestDto) {
        this.pageAndSortRequestDto = pageAndSortRequestDto;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

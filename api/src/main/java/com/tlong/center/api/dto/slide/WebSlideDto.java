package com.tlong.center.api.dto.slide;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("轮播图数据模型")
public class WebSlideDto implements Serializable {

    @ApiModelProperty("轮播图id")
    private Long id;

    @ApiModelProperty("广告标题")
    private String title;

    @ApiModelProperty("图片URL")
    private String picUrl;

    @ApiModelProperty("广告内容")
    private String content;

    @ApiModelProperty("发布人")
    private String name;

    @ApiModelProperty("是否审核")
    private Integer state;

    @ApiModelProperty("发布时间")
    private String publishTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}
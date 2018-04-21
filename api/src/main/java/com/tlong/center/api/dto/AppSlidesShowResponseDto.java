package com.tlong.center.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.List;

@ApiModel("轮播图返回数据模型")
public class AppSlidesShowResponseDto implements Serializable{
    private static final long serialVersionUID = 6180362655811659665L;

    @ApiModelProperty("url")
    private List<String> urls;

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}

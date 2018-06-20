package com.tlong.center.api.dto.web;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("订单请求模型")
public class EvaluateRequestDto implements Serializable {
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("商品名字")
    private String goodsName;

    @ApiModelProperty("商品编号")
    private String goodsCode;

    @ApiModelProperty("商品图片")
    private String goodsUrls;

    @ApiModelProperty("评论内容")
    private String content;

    @ApiModelProperty("评论图片")
    private String pics;

    @ApiModelProperty("星级")
    private String star;

    @ApiModelProperty("评论时间")
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public String getGoodsUrls() {
        return goodsUrls;
    }

    public void setGoodsUrls(String goodsUrls) {
        this.goodsUrls = goodsUrls;
    }
}

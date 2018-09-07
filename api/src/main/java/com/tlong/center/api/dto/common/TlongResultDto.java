package com.tlong.center.api.dto.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("天珑基本crud返回模型")
public class TlongResultDto implements Serializable {

    @ApiModelProperty("处理结果0成功 1失败 默认0")
    private Integer result = 0;

    @ApiModelProperty("返回内容")
    private String Content;

    public TlongResultDto() {
    }

    public TlongResultDto(Integer result) {
        this.result = result;
    }

    public TlongResultDto(String content) {
        Content = content;
    }

    public TlongResultDto(Integer result, String content) {
        this.result = result;
        Content = content;
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }
}

package com.tlong.center.api.dto.app.clazz;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel("APP课程类型返回列表")
public class ClazzStyleResponseDto {

    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("课程类型名称")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

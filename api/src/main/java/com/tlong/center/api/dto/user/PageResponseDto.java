package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.List;
@ApiModel("分页供应商模型")
public class PageResponseDto<T> implements Serializable{
    private List<T> list;
    private Integer count;

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }
}

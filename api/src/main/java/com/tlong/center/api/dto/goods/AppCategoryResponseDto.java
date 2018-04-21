package com.tlong.center.api.dto.goods;

import io.swagger.annotations.Api;

import java.io.Serializable;
import java.util.List;

@Api("商品类别列表返回模型")
public class AppCategoryResponseDto implements Serializable{
    private static final long serialVersionUID = -6442184984901664701L;

    //商品类别列表
    private List<String> categoryList;

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }
}

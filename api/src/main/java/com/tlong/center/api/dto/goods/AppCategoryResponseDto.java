package com.tlong.center.api.dto.goods;

import io.swagger.annotations.Api;

import java.io.Serializable;
import java.util.List;

@Api("商品类别列表返回模型")
public class AppCategoryResponseDto implements Serializable{
    private static final long serialVersionUID = -6442184984901664701L;

    //商品类别列表
    private Long goodsClassId;

    //商品费类型名称
    private String categoryName;

    public Long getGoodsClassId() {
        return goodsClassId;
    }

    public void setGoodsClassId(Long goodsClassId) {
        this.goodsClassId = goodsClassId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}

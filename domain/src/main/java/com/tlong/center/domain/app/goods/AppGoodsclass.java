package com.tlong.center.domain.app.goods;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "tlong_goods_class")
@DynamicUpdate
public class AppGoodsclass extends BaseJpa{


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品分类名称
    private String goodsClassName;

    //商品分类级别(1 一级 2二级)
    private Integer goodsClassLevel;

    //发布时间
    private String publishTime;

    //父分类id
    private Long goodsClassIdParent;

  /*  //当前状态(1启用 0禁用)
    private Integer curState = 1;

    //是否已删除(1已删除 0未删除)
    private Integer isDeleted = 0;*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppGoodsclass that = (AppGoodsclass) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGoodsClassName() {
        return goodsClassName;
    }

    public void setGoodsClassName(String goodsClassName) {
        this.goodsClassName = goodsClassName;
    }

    public Integer getGoodsClassLevel() {
        return goodsClassLevel;
    }

    public void setGoodsClassLevel(Integer goodsClassLevel) {
        this.goodsClassLevel = goodsClassLevel;
    }

    public Long getGoodsClassIdParent() {
        return goodsClassIdParent;
    }

    public void setGoodsClassIdParent(Long goodsClassIdParent) {
        this.goodsClassIdParent = goodsClassIdParent;
    }

   /* public Integer getCurState() {
        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }*/

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

}

package com.tlong.center.domain.app.goods;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_goodsclass")
@DynamicUpdate
public class AppGoodsclass extends BaseJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品分类名称
    private String className;

    //商品分类级别(1 一级 2二级)
    private Integer classLevel;

    //父分类id
    private Long parentClassId;

    //当前状态(1启用 0禁用)
    private Integer curState = 1;

    //是否已删除(1已删除 0未删除)
    private Integer isDeleted = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Integer getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Integer classLevel) {
        this.classLevel = classLevel;
    }

    public Long getParentClassId() {
        return parentClassId;
    }

    public void setParentClassId(Long parentClassId) {
        this.parentClassId = parentClassId;
    }

    public Integer getCurState() {
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
    }
}

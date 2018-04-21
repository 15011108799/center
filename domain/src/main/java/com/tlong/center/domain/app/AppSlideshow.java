package com.tlong.center.domain.app;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_slideshow")
@DynamicUpdate
public class AppSlideshow extends BaseJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //图片名称
    private String picName;

    //图片URL
    private String picUrl;

    //图片批次
    private Long picBatch;

    //当前状态(1启用 0禁用)
    private Integer curState = 1;

    //是否已删除(1已删除 0未删除)
    private Integer isDeleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Long getPicBatch() {
        return picBatch;
    }

    public void setPicBatch(Long picBatch) {
        this.picBatch = picBatch;
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

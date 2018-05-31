package com.tlong.center.domain.web;

import com.tlong.center.api.dto.slide.WebSlideDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_slide")
@DynamicUpdate
public class WebSlideshow extends BaseJpa {

    public WebSlideshow() {

    }

    public WebSlideshow(WebSlideDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }


    public WebSlideDto toDto() {
        WebSlideDto dto = new WebSlideDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //广告标题
    private String title;

    //图片URL
    private String picUrl;

    //广告内容
    private String content;

    //发布人
    private String name;

    //是否审核
    private Integer state;

    //发布时间
    private String publishTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }
}

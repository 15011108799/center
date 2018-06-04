package com.tlong.center.domain.web;

import com.tlong.center.api.dto.tlong.TlongRequestDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_about")
@DynamicUpdate
public class Tlong extends BaseJpa {
    public Tlong() {

    }

    public Tlong(TlongRequestDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public TlongRequestDto toDto() {
        TlongRequestDto dto = new TlongRequestDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //消息标题
    private String title;

    //消息内容
    private String content;

    //发布人
    private String userName;

    //审核状态
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

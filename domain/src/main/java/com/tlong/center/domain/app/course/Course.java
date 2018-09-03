package com.tlong.center.domain.app.course;

import com.tlong.center.api.dto.app.clazz.ClazzResponseDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_course")
@DynamicUpdate
public class Course {

    public Course(){}

    public ClazzResponseDto toClazzResponseDto(){
        ClazzResponseDto dto = new ClazzResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //课程分类id
    private Long styleId;

    //题目
    private String title;

    //课程目录
    private Integer catalog;

    // 课程讲师
    private String teacher;

    //课程介绍
    private String des;

    //课程视频
    private String video;

    //课程图片
    private String img;

    //当前状态
    private Integer curState;

    //发布时间
    private String publishTime;

    //newstime
    private String newstime;

    public String getNewstime() {
        return newstime;
    }

    public void setNewstime(String newstime) {
        this.newstime = newstime;
    }

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

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

    public Integer getCatalog() {
        return catalog;
    }

    public void setCatalog(Integer catalog) {
        this.catalog = catalog;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getCurState() {
        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }
}

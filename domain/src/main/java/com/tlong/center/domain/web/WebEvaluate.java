package com.tlong.center.domain.web;

import com.tlong.center.api.dto.web.EvaluateRequestDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_evaluate")
@DynamicUpdate
public class WebEvaluate extends BaseJpa {
    public WebEvaluate() {

    }

    public WebEvaluate(EvaluateRequestDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public EvaluateRequestDto toDto() {
        EvaluateRequestDto dto = new EvaluateRequestDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //商品id
    private Long goodsId;

    //用户id
    private Long userId;

    //评论内容
    private String content;

    //评论图片
    private String pics;

    //星级
    private String star;

    //评论时间
    private String time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

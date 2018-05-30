package com.tlong.center.domain.common.user;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_user_settings")
@DynamicUpdate
public class TlongUserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //用户id
    private Long userId;

    //商品发布数量
    private Integer goodsReleaseNumber;

    //商品重新发布数量
    private Integer goodsReReleaseNumber;

    //用户类型
    private Integer userType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getGoodsReleaseNumber() {
        return goodsReleaseNumber;
    }

    public void setGoodsReleaseNumber(Integer goodsReleaseNumber) {
        this.goodsReleaseNumber = goodsReleaseNumber;
    }

    public Integer getGoodsReReleaseNumber() {
        return goodsReReleaseNumber;
    }

    public void setGoodsReReleaseNumber(Integer goodsReReleaseNumber) {
        this.goodsReReleaseNumber = goodsReReleaseNumber;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }
}

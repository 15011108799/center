package com.tlong.center.api.dto.web.user;

public class UserRequestDto {
    private Long id;
    private Integer userType;
    private String goodsClass;
    private String className;
    private Long userId;

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public Integer getUserType() {
        return userType;
    }

    public String getGoodsClass() {
        return goodsClass;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
    }

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
}

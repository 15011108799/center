package com.tlong.center.api.dto;

import java.io.Serializable;

public class TlongUserInfos implements Serializable {

    //用户id
    private Long userId;

    //用户类型
    private String userClass;

    //是否是管理员
    private Integer isRoot;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserClass() {
        return userClass;
    }

    public void setUserClass(String userClass) {
        this.userClass = userClass;
    }

    public Integer getIsRoot() {
        return isRoot;
    }

    public void setIsRoot(Integer isRoot) {
        this.isRoot = isRoot;
    }
}

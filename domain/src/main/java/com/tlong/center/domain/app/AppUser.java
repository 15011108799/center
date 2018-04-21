package com.tlong.center.domain.app;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_user")
@DynamicUpdate
public class AppUser extends BaseJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //账户
    private String userName;

    //密码
    private String password;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

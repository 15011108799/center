package com.tlong.center.domain.web;

import javax.persistence.*;

@Entity
@Table(name = "tlong_role_power")
public class TlongRolePower {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //roleid
    private Long roleId;

    //powerId
    private Long powerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPowerId() {
        return powerId;
    }

    public void setPowerId(Long powerId) {
        this.powerId = powerId;
    }
}

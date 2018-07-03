package com.tlong.center.domain.web;

import com.tlong.center.api.dto.web.WebRoleDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_role")
@DynamicUpdate
public class TlongRole extends BaseJpa {


    public TlongRole() {

    }

    public TlongRole(WebRoleDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebRoleDto toDto() {
        WebRoleDto dto = new WebRoleDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //角色名称
    private String roleName;

    private String des;

    private String registDate;

    private Integer type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}

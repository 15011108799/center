package com.tlong.center.domain.web;

import com.tlong.center.api.dto.web.TlongPowerDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_power")
@DynamicUpdate
public class TlongPower extends BaseJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //权限名称
    private String powerName;

    //权限等级
    private Integer powerLevel;

    public TlongPowerDto toDto(){
        TlongPowerDto dto = new TlongPowerDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPowerName() {
        return powerName;
    }

    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    public Integer getPowerLevel() {
        return powerLevel;
    }

    public void setPowerLevel(Integer powerLevel) {
        this.powerLevel = powerLevel;
    }
}

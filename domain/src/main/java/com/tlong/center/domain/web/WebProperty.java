package com.tlong.center.domain.web;

import com.tlong.center.api.dto.web.WebPropertyDto;
import com.tlong.core.base.BaseJpa;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_property")
@DynamicUpdate
public class WebProperty extends BaseJpa {


    public WebProperty() {

    }

    public WebProperty(WebPropertyDto dto) {
        PropertyUtils.copyPropertiesOfNotNull(dto, this);
    }

    public WebPropertyDto toDto() {
        WebPropertyDto dto = new WebPropertyDto();
        PropertyUtils.copyPropertiesOfNotNull(this, dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String propertyName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }
}

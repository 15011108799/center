package com.tlong.center.domain.app.course;

import com.tlong.center.api.dto.app.clazz.ClazzStyleResponseDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_clazz_style")
@DynamicUpdate
public class ClazzStyle {

    public ClazzStyle() {
    }

    public ClazzStyleResponseDto toDto(){
        ClazzStyleResponseDto dto = new ClazzStyleResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //name
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.tlong.center.domain.common.code;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tlong_code")
@DynamicUpdate
public class TlongCode extends BaseJpa{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //编码
    private String code;

    //创建时间
    private Date createTime;

    //编码规则id
    private Long ruleId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getRuleId() {
        return ruleId;
    }

    public void setRuleId(Long ruleId) {
        this.ruleId = ruleId;
    }
}

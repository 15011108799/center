package com.tlong.center.domain.app.coupon;

import com.tlong.center.api.dto.app.coupon.CouponEffectResponsDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_coupon_effect")
@DynamicUpdate
public class CouponEffect {

    public CouponEffect() {
    }

    public CouponEffectResponsDto toDto(){
        CouponEffectResponsDto dto = new CouponEffectResponsDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //效果  优惠券效果0测试免单优惠券 1满100-50 2满100-10
    private Integer couponEffect;

    //卡券类型
    private Integer couponType;

    //效果名称
    private String effectName;

    public String getEffectName() {
        return effectName;
    }

    public void setEffectName(String effectName) {
        this.effectName = effectName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCouponEffect() {
        return couponEffect;
    }

    public void setCouponEffect(Integer couponEffect) {
        this.couponEffect = couponEffect;
    }
}

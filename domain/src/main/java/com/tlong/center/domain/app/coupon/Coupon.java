package com.tlong.center.domain.app.coupon;

import com.tlong.center.api.dto.app.coupon.AddCouponRequestDto;
import com.tlong.center.api.dto.app.coupon.CouponResponsDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "tlong_coupon")
@DynamicUpdate
public class Coupon {

    public Coupon() {
    }

    public CouponResponsDto toCouponDto(){
        CouponResponsDto dto = new CouponResponsDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    public Coupon(AddCouponRequestDto dto){
        PropertyUtils.copyPropertiesOfNotNull(dto,this);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //卡券名称
    private String couponName;

    //卡券类型
    private Integer couponType;

    //卡券过期时间
    private Long endTime;

    //卡券生产时间
    private Long newsTime;

    //卡券发行总数量
    private Integer countNumber;

    //卡券剩余数量
    private Integer remainNumber;

    //卡券描述
    private String couponDetail;

    //卡券效果id
    private Long couponEffectId;

    //卡券图片路径
    private String couponPic;

    //当前状态
    private Integer curState;

    //是否删除
    private Integer isDeleted;


    public String getCouponPic() {
        return couponPic;
    }

    public void setCouponPic(String couponPic) {
        this.couponPic = couponPic;
    }

    public Long getCouponEffectId() {
        return couponEffectId;
    }

    public void setCouponEffectId(Long couponEffectId) {
        this.couponEffectId = couponEffectId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Integer getCouponType() {
        return couponType;
    }

    public void setCouponType(Integer couponType) {
        this.couponType = couponType;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getNewsTime() {
        return newsTime;
    }

    public void setNewsTime(Long newsTime) {
        this.newsTime = newsTime;
    }

    public Integer getCountNumber() {
        return countNumber;
    }

    public void setCountNumber(Integer countNumber) {
        this.countNumber = countNumber;
    }

    public Integer getRemainNumber() {
        return remainNumber;
    }

    public void setRemainNumber(Integer remainNumber) {
        this.remainNumber = remainNumber;
    }

    public String getCouponDetail() {
        return couponDetail;
    }

    public void setCouponDetail(String couponDetail) {
        this.couponDetail = couponDetail;
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

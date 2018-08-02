package com.tlong.center.domain.app.check;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tlong_phone_checkout")
public class PhoneCheckOut {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //手机号码
    private String phone;

    //验证码
    private String checkOutCode;

    //有效截止时间
    private Long endTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCheckOutCode() {
        return checkOutCode;
    }

    public void setCheckOutCode(String checkOutCode) {
        this.checkOutCode = checkOutCode;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
}

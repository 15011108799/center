package com.tlong.center.domain.app;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "tlong_user")
@DynamicUpdate
public class TlongUser extends BaseJpa {

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

    //真实姓名
    private String realName;

    //创建人原始用户名
    private String orginPhone;

    //用户编号
    private String userCode;

    //性别
    private String sex;

    //用户类型（1 供应商  2 代理人）
    private Integer userType;

    //E签宝认证状态（1 认证通过  0 未认证）
    private Integer esgin;

    //加入时间
    private Date joinTime;

    //是否为集团类型(1 是  0 不是)
    private Integer isCompany;

    //头像地址
    private String headImage;

    //年龄
    private Integer age;

    //绑定手机号
    private String phone;

    //身份证号码
    private String idcardNumber;

    //身份证正面照
    private String idcardFront;

    //身份证反面照
    private String idcardReverse;

    //微信
    private String wx;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getOrginPhone() {
        return orginPhone;
    }

    public void setOrginPhone(String orginPhone) {
        this.orginPhone = orginPhone;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getEsgin() {
        return esgin;
    }

    public void setEsgin(Integer esgin) {
        this.esgin = esgin;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public String getHeadImage() {
        return headImage;
    }

    public void setHeadImage(String headImage) {
        this.headImage = headImage;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getIdcardFront() {
        return idcardFront;
    }

    public void setIdcardFront(String idcardFront) {
        this.idcardFront = idcardFront;
    }

    public String getIdcardReverse() {
        return idcardReverse;
    }

    public void setIdcardReverse(String idcardReverse) {
        this.idcardReverse = idcardReverse;
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx;
    }

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

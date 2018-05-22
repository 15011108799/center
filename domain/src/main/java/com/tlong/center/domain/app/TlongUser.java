package com.tlong.center.domain.app;

import com.tlong.core.base.BaseJpa;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    //性别
    private String sex;

    //用户类型（1 供应商  2 代理人）
    private Integer userType;

    //E签宝认证状态（1 认证通过  0 未认证）
    private Integer esgin;

    //加入时间
    private LocalDateTime joinTime;

    //是否为集团类型(1 是  0 不是)
    private Integer isCompany;

    //是否免检(1 免检  0 不是)
    private Integer isExemption;

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

    //昵称
    private String nickName;

    //服务热线
    private String serviceHotline;

    //后台认证
    private String authentication;

   //父级代理id
    private Long parentId;

    //代理人等级
    private Integer level;

    //经营地
    private String premises;

    //所属部门
    private String orgId;

    //用户编号
    private String userCode;

    //组织机构代码
    private String organizationCode;

    //社会统一信用代码
    private String succ;

    //法人姓名
    private String  legalPersonName;

    //营业执照照片
    private String businessLicense;

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }

    public String getOrganizationCode() {
        return organizationCode;
    }

    public void setOrganizationCode(String organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getSucc() {
        return succ;
    }

    public void setSucc(String succ) {
        this.succ = succ;
    }

    public String getLegalPersonName() {
        return legalPersonName;
    }

    public void setLegalPersonName(String legalPersonName) {
        this.legalPersonName = legalPersonName;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }

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

    public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }

    public Integer getIsExemption() {
        return isExemption;
    }

    public void setIsExemption(Integer isExemption) {
        this.isExemption = isExemption;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getServiceHotline() {
        return serviceHotline;
    }

    public void setServiceHotline(String serviceHotline) {
        this.serviceHotline = serviceHotline;
    }

    public String getAuthentication() {
        return authentication;
    }

    public void setAuthentication(String authentication) {
        this.authentication = authentication;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}

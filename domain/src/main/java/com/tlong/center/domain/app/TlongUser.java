package com.tlong.center.domain.app;

import com.tlong.center.api.dto.app.user.AppUserResponseDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.web.user.OrgManagerInfoResponseDto;
import com.tlong.core.utils.PropertyUtils;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tlong_user")
@DynamicUpdate
public class TlongUser implements Serializable {

    public TlongUser() {
    }

    public AppUserResponseDto toAppUserResponseDto() {
        AppUserResponseDto dto = new AppUserResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }

    public TlongUser (SuppliersRegisterRequsetDto requsetDto){
        PropertyUtils.copyPropertiesOfNotNull(requsetDto,this);
    }

    public OrgManagerInfoResponseDto toOrgManagerInfoResponseDto(){
        OrgManagerInfoResponseDto dto = new OrgManagerInfoResponseDto();
        PropertyUtils.copyPropertiesOfNotNull(this,dto);
        return dto;
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //账户
    private String userName;

    //父id
    private Long pid;

    //密码
    private String password;

    //用户编号
    private String userCode;

    //用户类型（1 供应商  2 代理人）
    private Integer userType;

    //E签宝认证状态（1 认证通过  0 未认证）
    private Integer esgin;

   /* //加入时间
    private LocalDateTime joinTime;*/

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
    private Integer authentication;

    //父级代理id
    private Long parentId;

    //代理人等级
    private Integer level;

    //E签宝证书编号
    private String evId;

    //当前状态(1启用 0禁用)
    private Integer curState = 1;
    //所属部门
    private Long orgId;

    //是否已删除(1已删除 0未删除)
    private Integer isDeleted;

    //创建时间
    private Date createDate;

    //组织机构代码
    private String organizationCode;

    //社会统一信用代码
    private String succ;

    //法人姓名
    private String  legalPersonName;

    //营业执照照片
    private String businessLicense;

    private String registDate;

    //商品发布个数
    private Integer goodsPublishNum;

    //出生年月
    private String birthday;

    //企业名称
    private String companyName;

    //所选大区
    private Integer area;

    //上货类别
    private String goodsClass;

    //真实姓名
    private String realName;

    //性别
    private String sex;

    //经营地
    private String premises;

    //newstime
    private String newstime;


    public String getNewstime() {
        return newstime;
    }

    public void setNewstime(String newstime) {
        this.newstime = newstime;
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

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
        this.userCode = userCode;
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

  /*  public LocalDateTime getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(LocalDateTime joinTime) {
        this.joinTime = joinTime;
    }*/

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public Integer getIsExemption() {
        return isExemption;
    }

    public void setIsExemption(Integer isExemption) {
        this.isExemption = isExemption;
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

    public Integer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Integer authentication) {
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

    public String getEvId() {
        return evId;
    }

    public void setEvId(String evId) {
        this.evId = evId;
    }

    public Integer getCurState() {
        return curState;
    }

    public void setCurState(Integer curState) {
        this.curState = curState;
    }

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public Integer getGoodsPublishNum() {
        return goodsPublishNum;
    }

    public void setGoodsPublishNum(Integer goodsPublishNum) {
        this.goodsPublishNum = goodsPublishNum;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public String getGoodsClass() {
        return goodsClass;
    }

    public void setGoodsClass(String goodsClass) {
        this.goodsClass = goodsClass;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }
}

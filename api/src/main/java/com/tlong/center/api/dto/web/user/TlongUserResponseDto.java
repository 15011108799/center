package com.tlong.center.api.dto.web.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel("天珑用户信息返回模型")
public class TlongUserResponseDto implements Serializable {

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("账户")
    private String userName;

    @ApiModelProperty("父id")
    private Long pid;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("用户类型（1 供应商  2 代理人）")
    private Integer userType;

    @ApiModelProperty("E签宝认证状态（1 认证通过  0 未认证）")
    private Integer esgin;

   /* //加入时间
    private LocalDateTime joinTime;*/

    @ApiModelProperty("是否为集团类型(1 是  0 不是)")
    private Integer isCompany;

    @ApiModelProperty("是否免检(1 免检  0 不是)")
    private Integer isExemption;

    @ApiModelProperty("头像地址")
    private String headImage;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("绑定手机号")
    private String phone;

    @ApiModelProperty("身份证号码")
    private String idcardNumber;

    //身份证正面照
    @ApiModelProperty("用户id")
    private String idcardFront;

    @ApiModelProperty("身份证反面照")
    private String idcardReverse;

    @ApiModelProperty("微信")
    private String wx;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("服务热线")
    private String serviceHotline;

    @ApiModelProperty("后台认证")
    private Integer authentication;

    @ApiModelProperty("父级代理id")
    private Long parentId;

    @ApiModelProperty("代理人等级")
    private Integer level;

    @ApiModelProperty("E签宝证书编号")
    private String evId;

    @ApiModelProperty("当前状态(1启用 0禁用)")
    private Integer curState = 1;

    @ApiModelProperty("所属部门")
    private Long orgId;

    @ApiModelProperty("是否已删除(1已删除 0未删除)")
    private Integer isDeleted;

    @ApiModelProperty("创建时间")
    private Date createDate;

    @ApiModelProperty("组织机构代码")
    private String organizationCode;

    @ApiModelProperty("社会统一信用代码")
    private String succ;

    @ApiModelProperty("法人姓名")
    private String  legalPersonName;

    @ApiModelProperty("营业执照照片")
    private String businessLicense;

    @ApiModelProperty("注册时间")
    private String registDate;

    @ApiModelProperty("商品发布个数")
    private Integer goodsPublishNum;

    @ApiModelProperty("出生年月")
    private String birthday;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("所选大区")
    private Integer area;

    @ApiModelProperty("上货类别")
    private String goodsClass;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("经营地")
    private String premises;

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

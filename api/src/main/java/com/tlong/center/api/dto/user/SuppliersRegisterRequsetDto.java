package com.tlong.center.api.dto.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("代理商注册请求模型")
public class SuppliersRegisterRequsetDto implements Serializable {
    private static final long serialVersionUID = -7697313937998312834L;
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("所选大区")
    private Integer area;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("用户类型")
    private Integer userType;

    @ApiModelProperty("是否是公司")
    private Integer isCompany;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("性别")
    private String sex;

    @ApiModelProperty("微信")
    private String wx;

    @ApiModelProperty("服务热线")
    private String serviceHotline;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像 url")
    private String headImage1;

    @ApiModelProperty("所属部门")
    private String orgId;

    @ApiModelProperty("经营地")
    private String premises;

    @ApiModelProperty("是否免检")
    private Integer isExemption;

    @ApiModelProperty("用户编号")
    private String userCode;

    @ApiModelProperty("组织机构代码")
    private String organizationCode;

    @ApiModelProperty("社会统一信用代码")
    private String succ;

    @ApiModelProperty("法人姓名")
    private String legalPersonName;

    @ApiModelProperty("营业执照照片")
    private String businessLicense1;

    @ApiModelProperty("绑定手机号")
    private String phone;

    @ApiModelProperty("反面照片")
    private String idcardReverse1;

    @ApiModelProperty("正面照片")
    private String idcardFront1;

    @ApiModelProperty("身份证号")
    private String idcardNumber;

    @ApiModelProperty("后台认证状态")
    private Integer authentication;

    @ApiModelProperty("e签宝认证状态")
    private Integer esgin;

    @ApiModelProperty("注册时间")
    private String registDate;

    @ApiModelProperty("商品发布个数")
    private Integer goodsPublishNum;

    @ApiModelProperty("出生年月")
    private String birthday;

    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("角色id")
    private Long roleId;

    @ApiModelProperty("角色名称")
    private String roleName;

    @ApiModelProperty("创始人旗舰店人数")
    private Integer agentOneNum;

    @ApiModelProperty("旗舰店人数")
    private Integer agentTwoNum;

    @ApiModelProperty("专卖店人数")
    private Integer agentThreeNum;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Integer getGoodsPublishNum() {
        return goodsPublishNum;
    }

    public void setGoodsPublishNum(Integer goodsPublishNum) {
        this.goodsPublishNum = goodsPublishNum;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public Integer getAuthentication() {
        return authentication;
    }

    public void setAuthentication(Integer authentication) {
        this.authentication = authentication;
    }

    public Integer getEsgin() {
        return esgin;
    }

    public void setEsgin(Integer esgin) {
        this.esgin = esgin;
    }

    public String getIdcardFront1() {
        return idcardFront1;
    }

    public void setIdcardFront1(String idcardFront1) {
        this.idcardFront1 = idcardFront1;
    }

    public String getIdcardNumber() {
        return idcardNumber;
    }

    public void setIdcardNumber(String idcardNumber) {
        this.idcardNumber = idcardNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsExemption() {
        return isExemption;
    }

    public void setIsExemption(Integer isExemption) {
        this.isExemption = isExemption;
    }

    public String getServiceHotline() {
        return serviceHotline;
    }

    public void setServiceHotline(String serviceHotline) {
        this.serviceHotline = serviceHotline;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }

    public String getPremises() {
        return premises;
    }

    public void setPremises(String premises) {
        this.premises = premises;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
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

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public Integer getIsCompany() {
        return isCompany;
    }

    public void setIsCompany(Integer isCompany) {
        this.isCompany = isCompany;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public String getHeadImage1() {
        return headImage1;
    }

    public void setHeadImage1(String headImage1) {
        this.headImage1 = headImage1;
    }

    public String getBusinessLicense1() {
        return businessLicense1;
    }

    public void setBusinessLicense1(String businessLicense1) {
        this.businessLicense1 = businessLicense1;
    }

    public String getIdcardReverse1() {
        return idcardReverse1;
    }

    public void setIdcardReverse1(String idcardReverse1) {
        this.idcardReverse1 = idcardReverse1;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Integer getArea() {
        return area;
    }

    public void setArea(Integer area) {
        this.area = area;
    }

    public Integer getAgentOneNum() {
        return agentOneNum;
    }

    public void setAgentOneNum(Integer agentOneNum) {
        this.agentOneNum = agentOneNum;
    }

    public Integer getAgentTwoNum() {
        return agentTwoNum;
    }

    public void setAgentTwoNum(Integer agentTwoNum) {
        this.agentTwoNum = agentTwoNum;
    }

    public Integer getAgentThreeNum() {
        return agentThreeNum;
    }

    public void setAgentThreeNum(Integer agentThreeNum) {
        this.agentThreeNum = agentThreeNum;
    }
}

package com.tlong.center.domain.app.esign;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tlong_esign_company")
@DynamicUpdate
public class EsignCompany implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //用户id
    private Long userId;

    //集团名称
    private String companyName;

    //企业编号
    private String codeOrg;

    //...
    private String codeUsc;

    //...
    private String legalName;

    //...
    private String legalidNo;

    //...
    private String name;

    //...
    private String cardNo;

    //...
    private String subbranch;

    //...
    private String bank;

    //...
    private String provice;

    //...
    private String city;

    //...
    private String serviceId;

    //...
    private Date time;

    //...
    private Date timePay;

    //企业类型
    private Integer organType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCodeOrg() {
        return codeOrg;
    }

    public void setCodeOrg(String codeOrg) {
        this.codeOrg = codeOrg;
    }

    public String getCodeUsc() {
        return codeUsc;
    }

    public void setCodeUsc(String codeUsc) {
        this.codeUsc = codeUsc;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalidNo() {
        return legalidNo;
    }

    public void setLegalidNo(String legalidNo) {
        this.legalidNo = legalidNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getSubbranch() {
        return subbranch;
    }

    public void setSubbranch(String subbranch) {
        this.subbranch = subbranch;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getProvice() {
        return provice;
    }

    public void setProvice(String provice) {
        this.provice = provice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getTimePay() {
        return timePay;
    }

    public void setTimePay(Date timePay) {
        this.timePay = timePay;
    }

    public Integer getOrganType() {
        return organType;
    }

    public void setOrganType(Integer organType) {
        this.organType = organType;
    }
}

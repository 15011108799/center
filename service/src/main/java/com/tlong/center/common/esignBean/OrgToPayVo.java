package com.tlong.center.common.esignBean;

import java.io.Serializable;

/**
 * Created by frank on 2017/6/27.
 */
public class OrgToPayVo implements Serializable{

    private String name;//对公账户户名(一般来说即企业名称

    private String cardno;//企业对公银行账号

    private String subbranch;//企业银行账号开户行支行全称

    private String bank;//企业银行账号开户行名称，支持银行列表见《e 签宝企业实名 认证服务支持打款银行列表》

    private String provice;//企业银行账号开户行所在省份，支持省份见《e 签宝企业实名 认证服务银行省市列表》

    private String city;//企业银行账号开户行所在城市，支持县市见《e 签宝企业实名 认证服务银行省市列表》

    private String notify;//打款完成通知的接收地址

    private String serviceId;//企业信息校验成功后返回的 serviceId

    private String prcptcd;//企业用户对公账户所在的开户行的大额行号;请参考3 支持银 行列表。

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
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

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getPrcptcd() {
        return prcptcd;
    }

    public void setPrcptcd(String prcptcd) {
        this.prcptcd = prcptcd;
    }
}

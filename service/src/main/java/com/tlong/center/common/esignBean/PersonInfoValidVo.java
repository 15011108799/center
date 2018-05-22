package com.tlong.center.common.esignBean;

import java.io.Serializable;

/**
 * Created by frank on 2017/6/27.
 */
public class PersonInfoValidVo implements Serializable{

    private String cardno;//银行卡号

    private String mobile;//银行预留手机号

    private String name;//姓名

    private String idno;//身份证号

    public String getCardno() {
        return cardno;
    }

    public void setCardno(String cardno) {
        this.cardno = cardno;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdno() {
        return idno;
    }

    public void setIdno(String idno) {
        this.idno = idno;
    }
}

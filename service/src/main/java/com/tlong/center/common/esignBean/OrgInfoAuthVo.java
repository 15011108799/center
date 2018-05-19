package com.tlong.center.common.esignBean;

import java.io.Serializable;

/**
 * Created by frank on 2017/6/27.
 */
public class OrgInfoAuthVo implements Serializable{

    private String name;//企业名称

    private String codeORG;//组织机构代码，codeORG、codeUSC 选填其中之一

    private String codeUSC;//社会统一信用代码，codeORG、codeUSC 选填其中之一

    private String legalName;//法人姓名

    private String legalIdno;//法人身份证号码

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeORG() {
        return codeORG;
    }

    public void setCodeORG(String codeORG) {
        this.codeORG = codeORG;
    }

    public String getCodeUSC() {
        return codeUSC;
    }

    public void setCodeUSC(String codeUSC) {
        this.codeUSC = codeUSC;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getLegalIdno() {
        return legalIdno;
    }

    public void setLegalIdno(String legalIdno) {
        this.legalIdno = legalIdno;
    }
}

package com.tlong.center.common.esignBean;

import java.io.Serializable;

/**
 * Created by frank on 2017/6/27.
 */
public class OrgPayAuthVo implements Serializable{


    private String serviceId;//OrgInfoAuth返回的serviceId

    private Float cash;//打款金额

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public Float getCash() {
        return cash;
    }

    public void setCash(Float cash) {
        this.cash = cash;
    }
}

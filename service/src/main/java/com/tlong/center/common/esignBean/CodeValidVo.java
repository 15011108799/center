package com.tlong.center.common.esignBean;

import java.io.Serializable;

/**
 * Created by frank on 2017/6/27.
 */
public class CodeValidVo implements Serializable{

    private String code;//短信验证码

    private String serviceId;//PersonInfoValid接口返回

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}

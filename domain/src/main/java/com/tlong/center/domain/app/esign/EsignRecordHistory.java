package com.tlong.center.domain.app.esign;

import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "tlong_esign_history")
@DynamicUpdate
public class EsignRecordHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    //调用的接口
    private String api;

    //请求的数据
    private String reqData;

    //返回的数据
    private String resData;

    //请求的时间
    private Date reqTime;

    //返回的时间
    private Date resTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getReqData() {
        return reqData;
    }

    public void setReqData(String reqData) {
        this.reqData = reqData;
    }

    public String getResData() {
        return resData;
    }

    public void setResData(String resData) {
        this.resData = resData;
    }

    public Date getReqTime() {
        return reqTime;
    }

    public void setReqTime(Date reqTime) {
        this.reqTime = reqTime;
    }

    public Date getResTime() {
        return resTime;
    }

    public void setResTime(Date resTime) {
        this.resTime = resTime;
    }
}

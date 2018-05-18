package com.tlong.center.api.dto.app;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("App更新检查返回模型")
public class AppUpdateCheckResponseDto implements Serializable {
    private static final long serialVersionUID = 2471754041047046581L;

    @ApiModelProperty("是否需要更新")
    private Integer needUpdate;

    @ApiModelProperty("新版本地址")
    private String newUrl;

    public Integer getNeedUpdate() {
        return needUpdate;
    }

    public void setNeedUpdate(Integer needUpdate) {
        this.needUpdate = needUpdate;
    }

    public String getNewUrl() {
        return newUrl;
    }

    public void setNewUrl(String newUrl) {
        this.newUrl = newUrl;
    }

    public AppUpdateCheckResponseDto(Integer needUpdate, String newUrl) {
        this.needUpdate = needUpdate;
        this.newUrl = newUrl;
    }

    public AppUpdateCheckResponseDto() {

    }
}

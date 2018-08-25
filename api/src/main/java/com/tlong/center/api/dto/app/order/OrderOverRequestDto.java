package com.tlong.center.api.dto.app.order;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("订单查询请求模型")
public class OrderOverRequestDto extends PageAndSortRequestDto implements Serializable{

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("订单状态")
    private Integer state;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}

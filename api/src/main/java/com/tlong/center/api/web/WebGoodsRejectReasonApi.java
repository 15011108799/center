package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.WebGoodsReasonResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api("商品驳回原因接口")
public interface WebGoodsRejectReasonApi {

    @ApiModelProperty("新增商品驳回原因")
    @PostMapping("/addGoodsRejectReason")
    Result addGoodsRejectReason(@RequestBody WebGoodsReasonResponseDto reasonResponseDto);

    @ApiModelProperty("查询商品驳回原因")
    @PostMapping("/findReason")
    WebGoodsReasonResponseDto findReason(@RequestParam Long id);

    @ApiModelProperty("删除驳回原因")
    @PostMapping("/delReason")
    Result delReason(@RequestParam Long id);

}

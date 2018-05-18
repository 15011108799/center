package com.tlong.center.api.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Api("获取页面信息接口")
public interface WebHtmlInfoApi {

    @ApiOperation("获取左侧panel列表")
    @PostMapping("/leftPanel")
    String leftPanel(@RequestParam Integer var1);
}

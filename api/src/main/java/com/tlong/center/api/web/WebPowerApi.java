package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.TlongPowerDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Api("权限增删改查接口")
public interface WebPowerApi {

    @ApiOperation("获取权限列表")
    @GetMapping("/powerList")
    List<TlongPowerDto> powerList();

    @ApiModelProperty("新增权限")
    @PostMapping("/addPower")
    Result addPower(@RequestBody TlongPowerDto reqDto);

    @ApiModelProperty("删除权限")
    @PutMapping("/delPower")
    Result delPower(@RequestParam Long powerId);

    @ApiModelProperty("修改权限信息")
    @PutMapping("/updatePower/{powerId}")
    Result updatePower(@RequestBody TlongPowerDto reqDto, @PathVariable Long powerId);

}

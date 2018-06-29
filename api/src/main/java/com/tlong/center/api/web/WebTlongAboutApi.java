package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.message.MessageSearchRequestDto;
import com.tlong.center.api.dto.tlong.TlongRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api("关于珑石管理接口")
public interface WebTlongAboutApi {
    @ApiOperation("添加关于珑石接口")
    @PostMapping("/addTlongshi")
    Result addTlongshi(@RequestBody TlongRequestDto requestDto);

    @ApiOperation("查询所有关于珑石接口")
    @PostMapping("/findAllTlongshi")
    PageResponseDto<TlongRequestDto> findAllTlongshi(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("删除关于珑石接口")
    @PutMapping("/delTlongshi")
    Result delTlongshi(@RequestParam Long id);

    @ApiOperation("批量删除关于珑石接口")
    @PutMapping("/delBatchTlongshi")
    Result delBatchTlongshi(@RequestParam String id);

    @ApiOperation("修改关于珑石接口")
    @PostMapping("/updateTlongshi")
    Result updateTlongshi(@RequestBody TlongRequestDto requestDto);

    @ApiOperation("修改关于珑石状态")
    @PostMapping("/updateTlongState")
    void updateTlongState(@RequestBody Long id);

    @ApiOperation("批量修改关于珑石状态")
    @PutMapping("/updateBatchTlongState")
    void updateBatchTlongState(@RequestParam String id);

    @ApiOperation("查找单条关于珑石接口")
    @PutMapping("/findTlongshiById")
    TlongRequestDto findTlongshiById(@RequestBody Long id);

    @ApiOperation("条件查询所有珑石接口")
    @PostMapping("/searchTlongshi")
    PageResponseDto<TlongRequestDto> searchTlongshi(@RequestBody MessageSearchRequestDto requestDto);
}

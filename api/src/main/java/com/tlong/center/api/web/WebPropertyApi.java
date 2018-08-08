package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebPropertyDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("商品属性管理接口")
public interface WebPropertyApi {
    @ApiOperation("添加属性接口")
    @PostMapping("/addProperty")
    Result addProperty(@RequestBody WebPropertyDto requestDto);

    @ApiOperation("查询属性接口")
    @PostMapping("/findAllProperty")
    PageResponseDto<WebPropertyDto> findAllProperty(@RequestBody PageAndSortRequestDto requestDto);

    @ApiModelProperty("删除商品属性")
    @PutMapping("/deleteProperty")
    Result deleteProperty(@RequestParam Long goodsId);

    @ApiModelProperty("查找商品属性")
    @PutMapping("/findPropertyById")
    WebPropertyDto findPropertyById(@RequestParam Long goodsId);

    @ApiModelProperty("查找商品属性")
    @PostMapping("/updateProperty")
    Result updateProperty(@RequestParam WebPropertyDto webPropertyDto);

    @ApiModelProperty("查找所有商品属性不分页")
    @PostMapping("/findAllPropertyNoPage")
    List<WebPropertyDto> findAllPropertyNoPage();
}

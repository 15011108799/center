package com.tlong.center.api.web;

import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.GoodsClassRequestDto;
import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api("分类管理接口")
public interface WebGoodsClassApi {

    @ApiOperation("查询所有分类接口")
    @PostMapping("/findAllGoodsClass")
    GoodsClassRequestDto findAllGoodsClass();

    @ApiOperation("添加分类")
    @PostMapping("/addGoodsClass")
    Result addGoodsClass(@RequestBody WebGoodsClassRequestDto requestDto);

    @ApiOperation("删除分类")
    @PostMapping("/delGoodsClass")
    Result delGoodsClass(@RequestBody Long id);

    @ApiOperation("查找单个分类")
    @PostMapping("/findGoodsTypeById")
    WebGoodsClassRequestDto findGoodsTypeById(@RequestBody Long id);

    @ApiOperation("修改分类接口")
    @PostMapping("/updateGoodsType")
    Result updateGoodsType(@RequestBody WebGoodsClassRequestDto requestDto);

    @ApiOperation("查询分类接口")
    @PostMapping("/findGoodsClass")
    List<GoodsTypeResponseDto> findGoodsClass();
}

package com.tlong.center.api.web;

import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.goods.GoodsTypeSearchRequestDto;
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

    @ApiOperation("查询所有价格倍率分类接口")
    @PostMapping("/findAllGoodsClassByName")
    GoodsClassRequestDto findAllGoodsClassByName(@RequestBody String goodsClassName);

    @ApiOperation("添加分类")
    @PostMapping("/addGoodsClass")
    Result addGoodsClass(@RequestBody WebGoodsClassRequestDto requestDto);

    @ApiOperation("删除分类")
    @PostMapping("/delGoodsClass")
    Result delGoodsClass(@RequestBody Long id);

    @ApiOperation("查找单个分类")
    @PostMapping("/findGoodsTypeById")
    WebGoodsClassRequestDto findGoodsTypeById(@RequestBody WebGoodsClassRequestDto requestDto);

    @ApiOperation("修改分类接口")
    @PostMapping("/updateGoodsType")
    Result updateGoodsType(@RequestBody WebGoodsClassRequestDto requestDto);

    @ApiOperation("查询一级分类接口")
    @PostMapping("/findGoodsClass")
    List<GoodsTypeResponseDto> findGoodsClass();

    @ApiOperation("查询二级分类接口")
    @PostMapping("/findGoodsTwoClass")
    List<GoodsTypeResponseDto> findGoodsTwoClass(@RequestBody  Long id);

    @ApiOperation("商品分类搜索")
    @PostMapping("/searchGoodsType")
    GoodsClassRequestDto searchGoodsType(@RequestBody GoodsTypeSearchRequestDto requestDto);
}

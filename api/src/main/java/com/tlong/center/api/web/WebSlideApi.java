package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.slide.WebSlideDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api("轮播图增删改查接口")
public interface WebSlideApi {

    @ApiOperation("获取轮播图列表")
    @GetMapping("/slideList")
    List<WebSlideDto> slideList();

    @ApiModelProperty("新增轮播图")
    @PostMapping("/addSlide")
    Result addSlide(@RequestBody WebSlideDto reqDto);

    @ApiModelProperty("删除轮播图")
    @PutMapping("/delSlide")
    Result delSlide(@RequestParam Long roleId);

    @ApiModelProperty("批量删除轮播图")
    @PutMapping("/delBatchSlide")
    Result delBatchSlide(@RequestParam String id);

    @ApiModelProperty("修改轮播图信息")
    @PostMapping("/updateSlide")
    Result updateSlide(@RequestBody WebSlideDto reqDto);

    @ApiOperation("修改轮播图状态")
    @PostMapping("/updateSlideState")
    void updateSlideState(@RequestBody Long id);

    @ApiOperation("批量修改轮播图状态")
    @PutMapping("/updateBatchSlideState")
    void updateBatchSlideState(@RequestParam String id);

    @ApiOperation("根据id查询轮播图")
    @PutMapping("/findSlideById")
    WebSlideDto findSlideById(@RequestParam Long id);

}

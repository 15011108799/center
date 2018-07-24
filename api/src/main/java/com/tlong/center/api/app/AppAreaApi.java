package com.tlong.center.api.app;

import com.tlong.center.api.dto.area.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("地域自定义接口")
public interface AppAreaApi {

    @ApiOperation("新增地域")
    @PostMapping("/addArea")  //TODO 需要注意设置下级的时候上级低于必须也要设置
    AppAddAreaResponseDto addArea(@RequestBody List<AppAddAreaRequestDto> requestDtos);

    @ApiOperation("查询地域接口")
    @PostMapping("/searchArea") //TODO 联动查询
    AppSearchAreaResponseDto searchArea(@RequestBody AppSearchAreaRequestDto requestDto);

    @ApiOperation("修改地域信息")
    @PostMapping("/updateArea")
    Boolean updateArea(@RequestBody AppUpdateAreaRequestDto requestDto);

    @ApiOperation("删除地域信息") //TODO 注意上下级关系
    @PostMapping("/deleteArea")
    Boolean deleteArea(@RequestParam Long areaId);
}

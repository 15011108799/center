package com.tlong.center.api.app;

import com.tlong.center.api.dto.AppSlidesShowResponseDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.AppCategoryResponseDto;
import com.tlong.center.api.dto.goods.AppIndexGoodsDetailResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Api("首页数据获取接口")
public interface IndexApi {

    @ApiOperation("获取轮播图")
    @PostMapping("/slideshow")
    AppSlidesShowResponseDto slideshow(@RequestParam Long picBatch);

    @ApiOperation("获取商品类别")
    @GetMapping("/category")
    AppCategoryResponseDto category();

    @ApiOperation("获取首页商品数据")
    @PostMapping("/indexGoodsDetail")
    Page<AppIndexGoodsDetailResponseDto> indexGoodsDetail(@RequestBody PageAndSortRequestDto requestDto);

}

package com.tlong.center.api.client.app;

import com.tlong.center.api.app.IndexApi;
import com.tlong.center.api.client.fallback.AppLoginClientFallback;
import com.tlong.center.api.dto.AppSlidesShowResponseDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.AppCategoryResponseDto;
import com.tlong.center.api.dto.goods.AppIndexGoodsDetailResponseDto;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.domain.Page;

@FeignClient(value = "boot",path = "/api/app",fallback = AppLoginClientFallback.class)
public interface IndexClient extends IndexApi{
//    @Override
//    public AppSlidesShowResponseDto slideshow(Long picBatch) {
//        return null;
//    }
//
//    @Override
//    public AppCategoryResponseDto category() {
//        return null;
//    }
//
//    @Override
//    public Page<AppIndexGoodsDetailResponseDto> indexGoodsDetail(PageAndSortRequestDto requestDto) {
//        return null;
//    }


}

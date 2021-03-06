package com.tlong.center.app;

import com.tlong.center.api.app.IndexApi;
import com.tlong.center.api.dto.AppSlidesShowResponseDto;
import com.tlong.center.api.dto.app.goods.AppIndexGoodsRequestDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.AppCategoryResponseDto;
import com.tlong.center.api.dto.app.goods.AppIndexGoodsResponseDto;
import com.tlong.center.service.IndexService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app/index")
public class IndexController implements IndexApi{

    final IndexService indexService;

    public IndexController(IndexService indexService) {
        this.indexService = indexService;
    }

    /**
     * 首页录播图获取方法
     */
    @Override
    public AppSlidesShowResponseDto slideshow(@RequestParam Long picBatch) {
        return indexService.slideshow(picBatch);
    }

    /**
     * 首页商品类别获取列表
     */
    @Override
    public List<AppCategoryResponseDto> category() {
        return indexService.category();
    }

    /**
     * 首页商品详情列表
     */
    @Override
    public Page<AppIndexGoodsResponseDto> indexGoodsDetail(@RequestBody AppIndexGoodsRequestDto requestDto) {
        return indexService.indexGoodsDetail(requestDto);
    }
}

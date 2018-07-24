package com.tlong.center.service;

import com.tlong.center.api.dto.AppSlidesShowResponseDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.AppCategoryResponseDto;
import com.tlong.center.api.dto.goods.AppIndexGoodsDetailResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.AppCategory;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.app.AppSlideshow;
import com.tlong.center.domain.repository.AppCategoryRepository;
import com.tlong.center.domain.repository.AppGoodsRepository;
import com.tlong.center.domain.repository.AppSlideshowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tlong.center.domain.app.QAppCategory.appCategory;
import static com.tlong.center.domain.app.QAppSlideshow.appSlideshow;
import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
import static com.tlong.center.domain.app.goods.QAppGoodsclass.appGoodsclass;



@Component
@Transactional
public class IndexService {

    @Autowired
    EntityManager entityManager;

    @Autowired
    AppSlideshowRepository appSlideshowRepository;

    @Autowired
    AppCategoryRepository appCategoryRepository;

    @Autowired
    AppGoodsRepository appGoodsRepository;


    /**
     * 获取轮播图方法
     */
    public AppSlidesShowResponseDto slideshow(Long picBatch) {
        Iterable<AppSlideshow> all = appSlideshowRepository.findAll(appSlideshow.curState.eq(1)
                .and(appSlideshow.picBatch.eq(picBatch)));

        List returnList = new ArrayList();
        all.forEach(one -> returnList.add(one.getPicUrl()));

        AppSlidesShowResponseDto responseDto = new AppSlidesShowResponseDto();
        responseDto.setUrls(returnList);
        return responseDto;
    }

    /**
     * 商品类别获取
     */
    public List<AppCategoryResponseDto> category() {
        Iterable<AppCategory> all = appCategoryRepository.findAll(appCategory.curState.ne(0));
        List<AppCategoryResponseDto> returnList = new ArrayList();

        all.forEach(one ->{
            AppCategoryResponseDto dto = new AppCategoryResponseDto();
            dto.setGoodsClassId(one.getGoodsClassId());
            dto.setCategoryName(one.getCategoryName());
            returnList.add(dto);
        } );
        return returnList;
    }

    /**
     * 首页商品详情
     */
    public Page<AppIndexGoodsDetailResponseDto> indexGoodsDetail(PageAndSortRequestDto requestDto) {
        //处理分页排序逻辑
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);

        Page<WebGoods> all = appGoodsRepository.findAll(webGoods.curState.eq(1)
                .and(webGoods.goodsClassId.eq(requestDto.getGoodsClassId())), pageRequest);

        //变换
        return all.map(one->{
            AppIndexGoodsDetailResponseDto responseDto = new AppIndexGoodsDetailResponseDto();
            responseDto.setId(one.getId());
            responseDto.setGoodsName(one.getGoodsHead());
            responseDto.setGoodsCode(one.getGoodsCode());
            responseDto.setFactoryPrice(one.getFactoryPrice());
            String[] split = one.getGoodsPic().split(",");
            responseDto.setPicUrl(split[0]);
            return responseDto;
        });
    }


    //字符串拆分为集合
    public List<String> stringToList(String str) {
        List list = new ArrayList();
        if (str != null && str.contains(",")) {
            String[] split = str.split(",");
            Arrays.asList(split);
        }
        list.add(str);
        return list;
    }


}

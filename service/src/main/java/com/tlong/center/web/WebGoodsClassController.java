package com.tlong.center.web;

import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.web.GoodsClassRequestDto;
import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import com.tlong.center.api.web.WebGoodsClassApi;
import com.tlong.center.service.GoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/web/goodsClass")
public class WebGoodsClassController implements WebGoodsClassApi{

    @Autowired
    private GoodsClassService goodsClassService;
    @Override
    public GoodsClassRequestDto findAllGoodsClass() {
        return goodsClassService.findAllGoodsClass();
    }

    @Override
    public GoodsClassRequestDto findAllGoodsClassByName(@RequestBody String goodsClassName) {
        return goodsClassService.findAllGoodsClassByName(goodsClassName);
    }

    @Override
    public Result addGoodsClass(@RequestBody WebGoodsClassRequestDto requestDto) {
        return goodsClassService.addGoodsClass(requestDto);
    }

    @Override
    public Result delGoodsClass(@RequestBody Long id) {
        return goodsClassService.delGoodsClass(id);
    }

    @Override
    public WebGoodsClassRequestDto findGoodsTypeById(@RequestBody WebGoodsClassRequestDto requestDto) {
        return goodsClassService.findGoodsTypeById(requestDto);
    }

    @Override
    public Result updateGoodsType(@RequestBody WebGoodsClassRequestDto requestDto) {
        return goodsClassService.updateGoodsType(requestDto);
    }

    @Override
    public List<GoodsTypeResponseDto> findGoodsClass() {
        return goodsClassService.findGoodsClass();
    }

    @Override
    public List<GoodsTypeResponseDto> findGoodsTwoClass(@RequestBody Long id) {
        return goodsClassService.findGoodsTwoClass(id);
    }
}

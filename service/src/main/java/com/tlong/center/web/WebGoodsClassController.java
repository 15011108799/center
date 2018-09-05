package com.tlong.center.web;

import com.tlong.center.api.dto.GoodsTypeResponseDto;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.goods.GoodsTypeSearchRequestDto;
import com.tlong.center.api.dto.web.GoodsClassRequestDto;
import com.tlong.center.api.dto.web.WebGoodsClassRequestDto;
import com.tlong.center.api.dto.web.user.UserRequestDto;
import com.tlong.center.api.web.WebGoodsClassApi;
import com.tlong.center.service.GoodsClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

    /**
     * 查询所有的一级分类
     */
    @Override
    public List<GoodsTypeResponseDto> findGoodsClassLevelOne() {
        return goodsClassService.findGoodsClassLevelOne();
    }

    /**
     * 根据一级分类id查询二级分类
     */
    @Override
    public List<GoodsTypeResponseDto> findGoodsLevelTwo(@PathVariable Long goodsClassId) {
        return goodsClassService.findGoodsLevelTwo(goodsClassId);
    }

    @Override
    public GoodsClassRequestDto searchGoodsType(@RequestBody GoodsTypeSearchRequestDto requestDto) {
        return goodsClassService.searchGoodsType(requestDto);
    }

    @Override
    public GoodsTypeResponseDto findOneGoodsClass(@RequestBody Long id) {
        return goodsClassService.findOneGoodsClass(id);
    }

    /**
     * 获取供应商可上货分类
     */
    @Override
    public List<GoodsTypeResponseDto> supplierGoodsClass(@PathVariable Long userId) {
        return goodsClassService.supplierGoodsClass(userId);
    }

    /**
     * 获取供应商可上货二级分类
     */
    @Override
    public List<GoodsTypeResponseDto> supplierGoodsClassLevelTwo(@PathVariable Long userId, @PathVariable Long goodsClassId) {
        return goodsClassService.supplierGoodsClassLevelTwo(userId,goodsClassId);
    }

    /**
     * 直接获取当前供应商所有的二级分类的列表
     */
    @Override
    public List<GoodsTypeResponseDto> selectAllGoodsClassLevelTwo(@PathVariable Long userId) {
        return goodsClassService.selectAllGoodsClassLevelTwo(userId);
    }
}

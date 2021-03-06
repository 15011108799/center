package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.GoodsSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.user.UserSearchRequestDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.center.api.dto.web.WebGoodsPageRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@Api("商品增删改查接口")
public interface WebGoodsApi {

    @ApiOperation("获取商品列表")
    @PostMapping("/findAllGoods")
    Page<WebGoodsDetailResponseDto> findAllGoods(@RequestBody WebGoodsPageRequestDto requestDto, @RequestParam MultiValueMap<String,String> params);

    @ApiModelProperty("新增商品")
    @PostMapping("/addGoods")
    Result addGoods(WebGoodsDetailResponseDto reqDto);

    @ApiModelProperty("删除商品")
    @PutMapping("/delGoods")
    Result delGoods(@RequestParam String goodsId);

    @ApiModelProperty("修改商品信息")
    @PostMapping("/updateGoods")
    Result updateGoods(@RequestParam("file") List<MultipartFile> file, WebGoodsDetailResponseDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);

    @ApiOperation("修改商品为通过状态")
    @PostMapping("/updateGoodsState")
    void updateGoodsState(@RequestBody Long id, @RequestParam Long checkUserId);

    @ApiOperation("修改商品为驳回状态")
    @PostMapping("/updateGoodsStateReject")
    void updateGoodsStateReject(@RequestBody Long id);

    @ApiOperation("根据id查询商品")
    @PutMapping("/findGoodsById")
    WebGoodsDetailResponseDto findGoodsById(@RequestParam String id);

    @ApiOperation("商品搜索")
    @PostMapping("/searchGoods")
    PageResponseDto<WebGoodsDetailResponseDto> searchGoods(@RequestBody GoodsSearchRequestDto requestDto);

    @ApiModelProperty("批量删除商品")
    @PutMapping("/delBatchGoods")
    Result delBatchGoods(@RequestParam String goodsId);

    @ApiModelProperty("重新发布商品")
    @PutMapping("/publishAgain")
    Result publishAgain(@RequestBody WebGoodsDetailResponseDto reqDto);
}

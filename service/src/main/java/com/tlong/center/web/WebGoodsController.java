package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.goods.GoodsSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import com.tlong.center.api.dto.web.WebGoodsPageRequestDto;
import com.tlong.center.api.web.WebGoodsApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.WebGoodsService;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequestMapping("/api/web/goods")
public class WebGoodsController implements WebGoodsApi {
    final WebGoodsService webGoodsService;

    public WebGoodsController(WebGoodsService webGoodsService) {
        this.webGoodsService = webGoodsService;
    }

    /**
     * 获取商品列表
     */
    @Override
    public Page<WebGoodsDetailResponseDto> findAllGoods(@RequestBody WebGoodsPageRequestDto requestDto, @RequestParam MultiValueMap<String,String> params) {
        return webGoodsService.findAllGoodsByPage(requestDto,params);
    }

    @Override
    public Result addGoods(WebGoodsDetailResponseDto reqDto) {
        return webGoodsService.add(reqDto);
    }

    @Override
    public Result delGoods(@RequestBody String goodsId) {
        return webGoodsService.delGoods(Long.valueOf(goodsId));
    }

    @Override
    public Result updateGoods(@RequestParam("file") List<MultipartFile> file, WebGoodsDetailResponseDto reqDto, @RequestParam String contentClass,@RequestParam String contentType) {
        return webGoodsService.updateGoods(FileUploadUtils.handleFileUpload(file,contentClass,contentType), reqDto);
    }

    @Override
    public void updateGoodsState(@RequestBody Long id, @RequestParam Long checkUserId) {
        webGoodsService.updateGoodsState(id,checkUserId);
    }

    @Override
    public void updateGoodsStateReject(@RequestBody Long id) {
        webGoodsService.updateGoodsStateReject(id);
    }

    @Override
    public WebGoodsDetailResponseDto findGoodsById(@RequestBody String id) {
        return webGoodsService.findGoodsById(Long.valueOf(id));
    }

    @Override
    public PageResponseDto<WebGoodsDetailResponseDto> searchGoods(@RequestBody GoodsSearchRequestDto requestDto) {
        return webGoodsService.searchGoods(requestDto);
    }

    @Override
    public Result delBatchGoods(@RequestBody String goodsId) {
        return webGoodsService.delBatchGoods(goodsId);
    }

    @Override
    public Result publishAgain(@RequestBody WebGoodsDetailResponseDto reqDto) {
        return webGoodsService.publishAgain(reqDto);
    }
}

package com.tlong.center.app;

import com.tlong.center.api.app.goods.AppGoodsApi;
import com.tlong.center.api.dto.app.goods.AppGoodsUploadRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.fileUpAndDown.FileController;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.AppGoodsService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/app/goods")
public class AppGoodsController implements AppGoodsApi {

    private final AppGoodsService appGoodsService;

    public AppGoodsController(AppGoodsService appGoodsService) {
        this.appGoodsService = appGoodsService;
    }

    /**
     * APP商品上传
     */
    @Override
    public TlongResultDto appGoodsUpload(@RequestBody AppGoodsUploadRequestDto reqDto) {
        return appGoodsService.appGoodsUpload(reqDto);
    }

    /**
     * 商品下架
     */
    @Override
    public TlongResultDto downGoods(@PathVariable Long goodsId) {
        return appGoodsService.downGoods(goodsId);
    }
}

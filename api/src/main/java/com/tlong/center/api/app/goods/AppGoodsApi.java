package com.tlong.center.api.app.goods;

import com.tlong.center.api.dto.app.goods.AppGoodsUploadRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("APP商品相关API")
public interface AppGoodsApi {

    @ApiOperation("商品上传接口")
    @PostMapping("/upload")
    TlongResultDto appGoodsUpload(@RequestParam("file") List<MultipartFile> file,@RequestBody AppGoodsUploadRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);
}

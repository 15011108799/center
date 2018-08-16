package com.tlong.center.api.common;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@Api("文件上传接口")
public interface UploadApi {

    @ApiOperation("单个文件上传")
    @PostMapping("/upload")
    String uploadFile(@RequestParam MultipartFile file, @RequestParam String contentType,@RequestParam String contentClass);

    @ApiOperation("批量文件上传")
    @PostMapping("/batchUpload")
    String handleFileUpload(HttpServletRequest request, @RequestParam String contentType,@RequestParam String contentClass); //TODO 需要更改为对象形式的参数集合
}

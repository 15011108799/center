package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.news.NewsRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Api("新闻增删改查接口")
public interface WebNewsApi {

    @ApiOperation("获取新闻列表")
    @PostMapping("/findAllNews")
    PageResponseDto<NewsRequestDto> findAllNews(@RequestBody PageAndSortRequestDto requestDto);

    @ApiModelProperty("新增新闻")
    @PostMapping("/addNews")
    Result addNews(@RequestParam("file") List<MultipartFile> file, NewsRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);

    @ApiModelProperty("删除新闻")
    @PutMapping("/delNews")
    Result delNews(@RequestParam String id);

    @ApiModelProperty("批量删除新闻")
    @PutMapping("/delBatchNews")
    Result delBatchNews(@RequestParam String id);

    @ApiModelProperty("修改新闻信息")
    @PostMapping("/updateNews")
    Result updateNews(@RequestParam("file") List<MultipartFile> file, NewsRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);

    @ApiOperation("修改新闻状态")
    @PostMapping("/updateNewsState")
    void updateNewsState(@RequestBody String id);

    @ApiOperation("批量修改新闻状态")
    @PutMapping("/updateBatchNewsState")
    void updateBatchNewsState(@RequestParam String id);

    @ApiOperation("根据id查询新闻")
    @PutMapping("/findNewsById")
    NewsRequestDto findNewsById(@RequestParam String id);
}

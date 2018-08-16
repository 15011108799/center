package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.news.NewsRequestDto;
import com.tlong.center.api.dto.partner.PartnerRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebNewsApi;
import com.tlong.center.api.web.WebPartnerApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.NewsService;
import com.tlong.center.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/web/news")
public class WebNewsController implements WebNewsApi {

    @Autowired
    private NewsService newsService;

    @Override
    public PageResponseDto<NewsRequestDto> findAllNews(@RequestBody PageAndSortRequestDto requestDto) {
        return newsService.findAllNews(requestDto);
    }

    @Override
    public Result addNews(@RequestParam("file") List<MultipartFile> file, NewsRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType) {
        return newsService.addNews(FileUploadUtils.handleFileUpload(file,contentClass,contentType), reqDto);
    }

    @Override
    public Result delNews(@RequestBody String id) {
        return newsService.delNews(Long.valueOf(id));
    }

    @Override
    public Result delBatchNews(@RequestBody String id) {
        return newsService.delBatchNews(id);
    }

    @Override
    public Result updateNews(@RequestParam("file") List<MultipartFile> file, NewsRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType) {
        return newsService.updateNews(FileUploadUtils.handleFileUpload(file,contentClass,contentType), reqDto);
    }

    @Override
    public void updateNewsState(@RequestBody String id) {
        newsService.updateNewsState(Long.valueOf(id));
    }

    @Override
    public void updateBatchNewsState(@RequestBody String id) {
        newsService.updateBatchNewsState(id);
    }

    @Override
    public NewsRequestDto findNewsById(@RequestBody String id) {
        return newsService.findNewsById(Long.valueOf(id));
    }
}

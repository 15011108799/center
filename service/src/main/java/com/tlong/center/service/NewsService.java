package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.news.NewsRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.repository.NewsRepository;
import com.tlong.center.domain.web.WebNews;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
@Transactional
public class NewsService {
    final EntityManager entityManager;
    final NewsRepository repository;

    public NewsService(EntityManager entityManager, NewsRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * 添加新闻
     *
     * @param s
     * @param reqDto
     * @return
     */
    public Result addNews(String s, NewsRequestDto reqDto) {
        reqDto.setPic(s.substring(0, s.length() - 1));
        reqDto.setTitleIcon(FileUploadUtils.readFile(reqDto.getTitleIcon()));
        reqDto.setVideo(FileUploadUtils.readFile(reqDto.getVideo()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        reqDto.setIsCheck("0");
        WebNews webNews = repository.save(new WebNews(reqDto));
        if (webNews != null)
            return new Result(1, "添加成功");
        else
            return new Result(0, "添加失败");
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    public Result delNews(Long id) {
        WebNews webNews = repository.findOne(id);
        if (webNews == null)
            return new Result(0, "删除失败");
        repository.delete(id);
        return new Result(1, "删除成功");
    }

    /**
     * 修改新闻信息
     *
     * @param s
     * @param reqDto
     * @return
     */
    public Result updateNews(String s, NewsRequestDto reqDto) {
        WebNews webNews1 = repository.findOne(Long.valueOf(reqDto.getId()));
        if (s.equals(""))
            reqDto.setPic(reqDto.getPic().substring(1));
        else
            reqDto.setPic(s.substring(0, s.length() - 1) + reqDto.getPic());
        String icon = FileUploadUtils.readFile(reqDto.getTitleIcon());
        if (icon != null && !icon.equals(""))
            reqDto.setTitleIcon(icon);
        else
            reqDto.setTitleIcon(webNews1.getTitleIcon());
        String video = FileUploadUtils.readFile(reqDto.getVideo());
        if (video != null && !video.equals(""))
            reqDto.setVideo(video);
        else
            reqDto.setVideo(webNews1.getVideo());
        WebNews webNews = new WebNews(reqDto);
        webNews.setIsCheck("0");
        webNews.setPublishTime(webNews1.getPublishTime());
        webNews.setId(Long.valueOf(reqDto.getId()));
        WebNews webNews2 = repository.save(webNews);
        if (webNews2 != null)
            return new Result(1, "修改成功");
        else
            return new Result(0, "修改失败");
    }

    /**
     * 更新审核状态
     *
     * @param id
     */
    public void updateNewsState(Long id) {
        WebNews webNews = repository.findOne(id);
        if ("0".equals(webNews.getIsCheck()))
            webNews.setIsCheck("1");
        else
            webNews.setIsCheck("0");
        repository.save(webNews);
    }

    /**
     * 查找合伙人通过id
     *
     * @param id
     * @return
     */
    public NewsRequestDto findNewsById(Long id) {
        WebNews webNews = repository.findOne(id);
        return webNews.toDto();
    }

    /**
     * 查询所有分页
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<NewsRequestDto> findAllNews(PageAndSortRequestDto requestDto) {
        PageResponseDto<NewsRequestDto> newsRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebNews> webNews = repository.findAll(pageRequest);
        List<NewsRequestDto> requestDtos = new ArrayList<>();
        webNews.forEach(webNews1 -> {
            NewsRequestDto newsRequestDto = webNews1.toDto();
            newsRequestDto.setId(webNews1.getId() + "");
            requestDtos.add(newsRequestDto);
        });
        newsRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebNews> webNews1 = repository.findAll();
        webNews1.forEach(webNews2 -> {
            count[0]++;
        });
        newsRequestDtoPageResponseDto.setCount(count[0]);
        return newsRequestDtoPageResponseDto;
    }
}

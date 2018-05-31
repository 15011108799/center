package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.slide.WebSlideDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.domain.repository.SlideRepository;
import com.tlong.center.domain.web.WebSlideshow;
import com.tlong.core.utils.PropertyUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
@Transactional
public class WebSlideService {

    final EntityManager entityManager;
    final SlideRepository repository;

    public WebSlideService(EntityManager entityManager, SlideRepository repository) {
        this.entityManager = entityManager;
        this.repository = repository;
    }

    /**
     * 轮播图列表查询
     */
    public List<WebSlideDto> slideList() {
        List<WebSlideshow> all = repository.findAll();
        List<WebSlideDto> webSlideDtos = new ArrayList<>();
        all.stream().forEach(one -> webSlideDtos.add(one.toDto()));
        return webSlideDtos;
    }

    /**
     * 轮播图新增
     */
    public Result addSlide(WebSlideDto reqDto) {
        reqDto.setPicUrl(FileUploadUtils.readFile(reqDto.getPicUrl()));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        reqDto.setPublishTime(simpleDateFormat.format(new Date()));
        WebSlideshow webSlideshow = repository.save(new WebSlideshow(reqDto));
        if (webSlideshow != null)
            return new Result(1, "添加成功");
        else
            return new Result(0, "添加失败");
    }

    /**
     * 删除轮播图
     */
    public Result delSlide(Long slideId) {
        WebSlideshow one = repository.findOne(slideId);
        if (Objects.nonNull(one)) {
            repository.delete(slideId);
            return new Result(1, "删除成功");
        }
        return new Result(0, "要删除的轮播图不存在");
    }

    /**
     * 修改轮播图信息
     */
    public Result updateSlide(WebSlideDto reqDto) {
        WebSlideshow one = repository.findOne(reqDto.getId());
        if (FileUploadUtils.readFile(reqDto.getPicUrl()) == null || FileUploadUtils.readFile(reqDto.getPicUrl()).equals(""))
            reqDto.setPicUrl(one.getPicUrl());
        else
            reqDto.setPicUrl(FileUploadUtils.readFile(reqDto.getPicUrl()));
        if (Objects.nonNull(one)) {
            PropertyUtils.copyPropertiesOfNotNull(reqDto, one);
            repository.save(one);
            return new Result(1, "修改成功");
        }
        return new Result(0, "要修改的的轮播图不存在");
    }

    /**
     * 修改轮播图审核状态
     *
     * @param id
     */
    public void updateSlideState(Long id) {
        WebSlideshow slideshow = repository.findOne(id);
        if (slideshow.getState() == 0)
            slideshow.setState(1);
        else
            slideshow.setState(0);
        repository.save(slideshow);
    }

    /**
     * 查找单条轮播图
     *
     * @param id
     * @return
     */
    public WebSlideDto findSlideById(Long id) {
        WebSlideshow slideshow = repository.findOne(id);
        return slideshow.toDto();
    }
}

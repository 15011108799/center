package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.slide.WebSlideDto;
import com.tlong.center.api.web.WebSlideApi;
import com.tlong.center.service.WebSlideService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/web/slide")
public class WebSlideController implements WebSlideApi {


    final WebSlideService webSlideService;

    public WebSlideController(WebSlideService webSlideService) {
        this.webSlideService = webSlideService;
    }

    /**
     * 轮播图列表查询
     */
    @Override
    public List<WebSlideDto> slideList() {
        return webSlideService.slideList();
    }

    /**
     * 新增轮播图
     */
    @Override
    public Result addSlide(@RequestBody WebSlideDto reqDto) {
        return webSlideService.addSlide(reqDto);
    }

    /**
     * 删除轮播图
     */
    @Override
    public Result delSlide(@RequestBody Long slideId) {
        return webSlideService.delSlide(slideId);
    }

    /**
     * 修改轮播图
     */
    @Override
    public Result updateSlide(@RequestBody WebSlideDto reqDto) {
        return webSlideService.updateSlide(reqDto);
    }

    /**
     * 修改轮播图审核状态
     * @param id
     */
    @Override
    public void updateSlideState(@RequestBody Long id) {
        webSlideService.updateSlideState(id);
    }

    /**
     * 根据id查找轮播图信息
     * @param id
     * @return
     */
    @Override
    public WebSlideDto findSlideById(@RequestBody Long id) {
        return webSlideService.findSlideById(id);
    }
}

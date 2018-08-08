package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebPropertyDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.course.Course;
import com.tlong.center.domain.repository.WebPropertyRepository;
import com.tlong.center.domain.web.WebProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Component
@Transactional
public class PropertyService {
    @Autowired
    private WebPropertyRepository webPropertyRepository;

    /**
     * 添加商品属性
     *
     * @param requestDto
     * @return
     */
    public Result addProperty(WebPropertyDto requestDto) {
        WebProperty webProperty = new WebProperty(requestDto);
        WebProperty save = webPropertyRepository.save(webProperty);
        if (save == null)
            return new Result(0, "添加失败");
        return new Result(1, "添加成功");
    }

    /**
     * 查询所有属性
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<WebPropertyDto> findAllProperty(PageAndSortRequestDto requestDto) {
        PageResponseDto<WebPropertyDto> webPropertyDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebProperty> webProperties = webPropertyRepository.findAll(pageRequest);
        List<WebPropertyDto> requestDtos = new ArrayList<>();
        webProperties.forEach(property -> {
            WebPropertyDto webPropertyDto = property.toDto();
            requestDtos.add(webPropertyDto);
        });
        webPropertyDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebProperty> webProperties1 = webPropertyRepository.findAll();
        webProperties1.forEach(property -> {
            count[0]++;
        });
        webPropertyDtoPageResponseDto.setCount(count[0]);
        return webPropertyDtoPageResponseDto;
    }

    /**
     * 删除商品属性
     *
     * @param goodsId
     * @return
     */
    public Result deleteProperty(Long goodsId) {
        WebProperty one = webPropertyRepository.findOne(goodsId);
        if (one == null)
            return new Result(0, "删除失败");
        webPropertyRepository.delete(goodsId);
        return new Result(1, "删除成功");
    }

    /**
     * 查找商品属性
     *
     * @param goodsId
     * @return
     */
    public WebPropertyDto findPropertyById(Long goodsId) {
        WebProperty one = webPropertyRepository.findOne(goodsId);
        return one.toDto();
    }

    /**
     * 修改属性
     *
     * @param webPropertyDto
     * @return
     */
    public Result updateProperty(WebPropertyDto webPropertyDto) {
        WebProperty webProperty = new WebProperty(webPropertyDto);
        WebProperty save = webPropertyRepository.save(webProperty);
        if (save == null)
            return new Result(0, "修改失败");
        return new Result(1, "修改成功");
    }

    /**
     * 查找所有商品属性不分页
     *
     * @return
     */
    public List<WebPropertyDto> findAllPropertyNoPage() {
        List<WebProperty> all = webPropertyRepository.findAll();
        List<WebPropertyDto> webPropertyDtos = new ArrayList<>();
        all.forEach(one -> {
            webPropertyDtos.add(one.toDto());
        });
        return webPropertyDtos;
    }
}

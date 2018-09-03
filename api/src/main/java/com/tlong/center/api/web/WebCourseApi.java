package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.course.CourseSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebGoodsDetailResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.util.List;


@Api("课程管理接口")
public interface WebCourseApi {
    @ApiModelProperty("课程添加接口")
    @PostMapping("/addCourse")
    Result addCourse(AppCourseRequestDto reqDto);

    @ApiOperation("课程查询接口")
    @PostMapping("/findAllCourse")
    PageResponseDto<AppCourseRequestDto> findAllCourse(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("删除课程接口")
    @PutMapping("/delCourse")
    Result delCourse(@RequestParam Long id);


    @ApiOperation("修改课程接口")
    @PostMapping("/updateCourse")
    Result updateCourse(@RequestParam("file") MultipartFile file, AppCourseRequestDto reqDto, @RequestParam String contentClass,@RequestParam String contentType);


    @ApiOperation("查找单条课程接口")
    @PutMapping("/findCourseById")
    AppCourseRequestDto findCourseById(@RequestBody Long id);

    @ApiOperation("条件查询所有课程接口")
    @PostMapping("/searchCourse")
    PageResponseDto<AppCourseRequestDto> searchCourse(@RequestBody CourseSearchRequestDto requestDto);

    @ApiOperation("批量删除课程接口")
    @PutMapping("/delBatchCourse")
    Result delBatchCourse(@RequestParam String id);
}


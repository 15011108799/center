package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("课程管理接口")
public interface WebCourseApi {
    @ApiOperation("课程添加接口")
    @PostMapping("/addCourse")
    Result addCourse(@RequestBody AppCourseRequestDto requestDto);

    @ApiOperation("课程查询接口")
    @PostMapping("/findAllCourse")
    List<AppCourseRequestDto> findAllCourse();

    @ApiOperation("删除课程接口")
    @PutMapping("/delCourse")
    Result delCourse(@RequestParam Long id);


    @ApiOperation("修改课程接口")
    @PostMapping("/updateCourse")
    Result updateCourse(@RequestBody AppCourseRequestDto requestDto);


    @ApiOperation("查找单条课程接口")
    @PutMapping("/findCourseById")
    AppCourseRequestDto findCourseById(@RequestBody Long id);
}

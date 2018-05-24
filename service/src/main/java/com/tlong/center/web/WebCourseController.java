package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.web.WebCourseApi;
import com.tlong.center.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/web/course")
public class WebCourseController implements WebCourseApi {
    @Autowired
    private CourseService courseService;

    @Override
    public Result addCourse(@RequestBody  AppCourseRequestDto requestDto) {
        return courseService.addCourse(requestDto);
    }

    @Override
    public List<AppCourseRequestDto> findAllCourse() {
        return courseService.findAllCourse();
    }

    @Override
    public Result delCourse(@RequestBody Long id) {
        return courseService.delCourse(id);
    }

    @Override
    public Result updateCourse(@RequestBody AppCourseRequestDto requestDto) {
        return courseService.updateCourse(requestDto);
    }

    @Override
    public AppCourseRequestDto findCourseById(@RequestBody Long id) {
        return courseService.findCourseById(id);
    }
}

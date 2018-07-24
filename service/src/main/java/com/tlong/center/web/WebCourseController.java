package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.course.CourseSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.web.WebCourseApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.CourseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/web/course")
public class WebCourseController implements WebCourseApi {
    @Autowired
    private CourseService courseService;

    @Override
    public Result addCourse(@RequestParam("file") MultipartFile file, AppCourseRequestDto reqDto) {
        String filePath = FileUploadUtils.upload(file);
        if (StringUtils.isNotEmpty(filePath))
            reqDto.setVideo(filePath.substring(0, filePath.length() - 1));
        return courseService.addCourse(reqDto);
    }

    @Override
    public PageResponseDto<AppCourseRequestDto> findAllCourse(@RequestBody PageAndSortRequestDto requestDto) {
        return courseService.findAllCourse(requestDto);
    }

    @Override
    public Result delCourse(@RequestBody Long id) {
        return courseService.delCourse(id);
    }

    @Override
    public Result updateCourse(@RequestParam("file") MultipartFile file, AppCourseRequestDto reqDto) {
        if (file!=null) {
            String filePath = FileUploadUtils.upload(file);
            if (StringUtils.isNotEmpty(filePath))
                reqDto.setVideo(filePath.substring(0, filePath.length() - 1));
        }
        return courseService.updateCourse(reqDto);
    }

    @Override
    public AppCourseRequestDto findCourseById(@RequestBody Long id) {
        return courseService.findCourseById(id);
    }

    @Override
    public PageResponseDto<AppCourseRequestDto> searchCourse(@RequestBody CourseSearchRequestDto requestDto) {
        return courseService.searchCourse(requestDto);
    }

    @Override
    public Result delBatchCourse(@RequestBody String id) {
        return courseService.delBatchCourse(id);
    }
}

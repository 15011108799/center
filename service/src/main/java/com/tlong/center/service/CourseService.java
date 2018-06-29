package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.course.CourseSearchRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.Message;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.course.Course;
import com.tlong.center.domain.app.course.QCourse;
import com.tlong.center.domain.repository.AppCourseRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.course.QCourse.course;

@Component
@Transactional
public class CourseService {
    @Autowired
    private AppCourseRepository appCourseRepository;

    public Result addCourse(AppCourseRequestDto requestDto) {
        Course course = new Course();
        course.setTitle(requestDto.getTitle());
        course.setCatalog(requestDto.getCatalog());
        course.setCurState(requestDto.getCurState());
        course.setDes(requestDto.getDes());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        course.setPublishTime(simpleDateFormat.format(new Date()));
        course.setTeacher(requestDto.getTeacher());
        if (!requestDto.getVideo().equals(""))
            course.setVideo(FileUploadUtils.readFile(requestDto.getVideo()));
        if (!requestDto.getImg().equals(""))
            course.setImg(FileUploadUtils.readFile(requestDto.getImg()));
        Course course1 = appCourseRepository.save(course);
        if (course1 == null) {
            return new Result(0, "添加失败");
        }
        return new Result(1, "添加成功");
    }

    public PageResponseDto<AppCourseRequestDto> findAllCourse(PageAndSortRequestDto requestDto) {
        PageResponseDto<AppCourseRequestDto> courseRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<Course> courses = appCourseRepository.findAll(pageRequest);
        List<AppCourseRequestDto> requestDtos = new ArrayList<>();
        courses.forEach(course -> {
            AppCourseRequestDto courseRequestDto = new AppCourseRequestDto();
            courseRequestDto.setId(course.getId());
            courseRequestDto.setTitle(course.getTitle());
            courseRequestDto.setCatalog(course.getCatalog());
            courseRequestDto.setDes(course.getDes());
            courseRequestDto.setCurState(course.getCurState());
            courseRequestDto.setPublishTime(course.getPublishTime());
            courseRequestDto.setVideo(course.getVideo());
            courseRequestDto.setImg(course.getImg());
            courseRequestDto.setTeacher(course.getTeacher());
            requestDtos.add(courseRequestDto);
        });
        courseRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Course> courses1 = appCourseRepository.findAll();
        courses1.forEach(course -> {
            count[0]++;
        });
        courseRequestDtoPageResponseDto.setCount(count[0]);
        return courseRequestDtoPageResponseDto;
    }

    public Result delCourse(Long id) {
        Course course = appCourseRepository.findOne(id);
        if (course == null)
            return new Result(0, "删除课程失败");
        appCourseRepository.delete(id);
        return new Result(1, "删除成功");
    }

    public Result updateCourse(AppCourseRequestDto requestDto) {
        Course course = appCourseRepository.findOne(requestDto.getId());
        Course course1 = new Course();
        course1.setId(requestDto.getId());
        course1.setTitle(requestDto.getTitle());
        course1.setCatalog(requestDto.getCatalog());
        course1.setTeacher(requestDto.getTeacher());
        course1.setCurState(requestDto.getCurState());
        course1.setDes(requestDto.getDes());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        course1.setPublishTime(simpleDateFormat.format(new Date()));
        if (requestDto.getVideo() == null || FileUploadUtils.readFile(requestDto.getVideo()).equals(""))
            course1.setVideo(course.getVideo());
        else
            course1.setVideo(FileUploadUtils.readFile(requestDto.getVideo()));
        if (requestDto.getImg() == null || FileUploadUtils.readFile(requestDto.getImg()).equals(""))
            course1.setImg(course.getImg());
        else
            course1.setImg(FileUploadUtils.readFile(requestDto.getImg()));
        Course course2 = appCourseRepository.save(course1);
        if (course2 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");
    }

    public AppCourseRequestDto findCourseById(Long id) {
        Course course = appCourseRepository.findOne(id);
        AppCourseRequestDto requestDto = new AppCourseRequestDto();
        requestDto.setTitle(course.getTitle());
        requestDto.setCatalog(course.getCatalog());
        requestDto.setTeacher(course.getTeacher());
        requestDto.setDes(course.getDes());
        requestDto.setCurState(course.getCurState());
        requestDto.setVideo(course.getVideo());
        requestDto.setImg(course.getImg());
        return requestDto;
    }

    public PageResponseDto<AppCourseRequestDto> searchCourse(CourseSearchRequestDto requestDto) {
        PageResponseDto<AppCourseRequestDto> courseRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Predicate pre = course.id.isNotNull();
        if (StringUtils.isNotEmpty(requestDto.getTitle()))
            pre = ExpressionUtils.and(pre, course.title.eq(requestDto.getTitle()));
        if (requestDto.getCatalog() != 5)
            pre = ExpressionUtils.and(pre, course.catalog.intValue().eq(requestDto.getCatalog()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, course.publishTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, course.publishTime.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, course.publishTime.gt(requestDto.getStartTime() + " 00:00:00"));
        Page<Course> courses = appCourseRepository.findAll(pre,pageRequest);
        List<AppCourseRequestDto> requestDtos = new ArrayList<>();
        courses.forEach(course -> {
            AppCourseRequestDto courseRequestDto = new AppCourseRequestDto();
            courseRequestDto.setId(course.getId());
            courseRequestDto.setTitle(course.getTitle());
            courseRequestDto.setCatalog(course.getCatalog());
            courseRequestDto.setDes(course.getDes());
            courseRequestDto.setCurState(course.getCurState());
            courseRequestDto.setPublishTime(course.getPublishTime());
            courseRequestDto.setVideo(course.getVideo());
            courseRequestDto.setImg(course.getImg());
            courseRequestDto.setTeacher(course.getTeacher());
            requestDtos.add(courseRequestDto);
        });
        courseRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<Course> courses1 = appCourseRepository.findAll(pre);
        courses1.forEach(course -> {
            count[0]++;
        });
        courseRequestDtoPageResponseDto.setCount(count[0]);
        return courseRequestDtoPageResponseDto;
    }
}

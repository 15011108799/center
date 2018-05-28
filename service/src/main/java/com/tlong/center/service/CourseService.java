package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.course.AppCourseRequestDto;
import com.tlong.center.api.dto.message.MessageRequestDto;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.domain.app.Message;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.course.Course;
import com.tlong.center.domain.repository.AppCourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public List<AppCourseRequestDto> findAllCourse() {
        List<Course> courses = appCourseRepository.findAll();
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
        return requestDtos;
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
        if (requestDto.getVideo()==null||FileUploadUtils.readFile(requestDto.getVideo()).equals(""))
            course1.setVideo(course.getVideo());
        else
            course1.setVideo(FileUploadUtils.readFile(requestDto.getVideo()));
        if (requestDto.getImg()==null||FileUploadUtils.readFile(requestDto.getImg()).equals(""))
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
        Course course=appCourseRepository.findOne(id);
        AppCourseRequestDto requestDto=new AppCourseRequestDto();
        requestDto.setTitle(course.getTitle());
        requestDto.setCatalog(course.getCatalog());
        requestDto.setTeacher(course.getTeacher());
        requestDto.setDes(course.getDes());
        requestDto.setCurState(course.getCurState());
        requestDto.setVideo(course.getVideo());
        requestDto.setImg(course.getImg());
        return requestDto;
    }
}

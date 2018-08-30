package com.tlong.center.service;

import com.tlong.center.api.dto.app.clazz.ClazzResponseDto;
import com.tlong.center.api.dto.app.clazz.ClazzStyleResponseDto;
import com.tlong.center.api.dto.app.user.AppUserLoginRequestDto;
import com.tlong.center.api.dto.app.user.AppUserLoginResponseDto;
import com.tlong.center.api.dto.app.user.AppUserResponseDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.course.ClazzStyle;
import com.tlong.center.domain.app.course.Course;
import com.tlong.center.domain.repository.ClazzStyleRepository;
import com.tlong.center.domain.repository.CourseRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.course.QCourse.course;


@Component
@Transactional
public class AppService {

    private final TlongUserRepository tlongUserRepository;
    private final ClazzStyleRepository clazzStyleRepository;
    private final CourseRepository courseRepository;

    @Autowired
    public AppService(TlongUserRepository tlongUserRepository, ClazzStyleRepository clazzStyleRepository, CourseRepository courseRepository) {
        this.tlongUserRepository = tlongUserRepository;
        this.clazzStyleRepository = clazzStyleRepository;
        this.courseRepository = courseRepository;
    }

    /**
     * app登录业务
     */
    public AppUserLoginResponseDto appLogin(AppUserLoginRequestDto requestDto){
        //MD5加密后的密码
        String md5Password = MD5Util.MD5(requestDto.getPassword());
        TlongUser one;
        AppUserLoginResponseDto responseDto = new AppUserLoginResponseDto();
        //使用非加密密码进行登陆
        one = tlongUserRepository.findOne(tlongUser.userName.eq(requestDto.getUserName())
                .and(tlongUser.password.eq(requestDto.getPassword())));
        if (Objects.isNull(one)){
            one = tlongUserRepository.findOne(tlongUser.userName.eq(requestDto.getUserName())
                    .and(tlongUser.password.eq(md5Password)));
            if (Objects.isNull(one)){
                responseDto.setFlag(1);
                return responseDto;
            }
        }
        responseDto.setOrgId(one.getOrgId());
        responseDto.setFlag(0);
        responseDto.setUserType(one.getUserType());
        responseDto.setEsgin(one.getEsgin());
        responseDto.setUserId(one.getId());
        responseDto.setIsCompany(one.getIsCompany());
        responseDto.setIsChecked(one.getAuthentication());
        return responseDto;
    }

    /**
     * 根据用户id获取用户基本信息
     */
    public AppUserResponseDto userInfo(Long userId) {
        TlongUser one = tlongUserRepository.findOne(userId);
        AppUserResponseDto responseDto = new AppUserResponseDto();
        if (!Objects.isNull(one)){
           responseDto.setAge(one.getAge());
           if (one.getSex() != null) {
               responseDto.setSex(Integer.valueOf(one.getSex()));
           }
           responseDto.setAuthentication(one.getAuthentication());
           responseDto.setEsgin(one.getEsgin());
           responseDto.setOrgId(one.getOrgId());
           responseDto.setEvId(one.getEvId());
           responseDto.setUserCode(one.getUserCode());
           responseDto.setHeadImage(one.getHeadImage());
           responseDto.setPhone(one.getPhone());
           responseDto.setNickName(one.getNickName());
           responseDto.setServiceHotline(one.getServiceHotline());
           responseDto.setGoodsClass(one.getGoodsClass());
           responseDto.setUserId(userId);
           responseDto.setWx(one.getWx());
        }
        responseDto.setUserId(0L);
        return responseDto;
    }

    /**
     * 根据用户id获取下级代理商信息
     */
    public List<AppUserResponseDto> children(Long userId) {
        Iterable<TlongUser> all = tlongUserRepository.findAll(tlongUser.parentId.eq(userId));
        List<AppUserResponseDto> responseDtoList = new ArrayList<>();
        all.forEach(one -> responseDtoList.add(one.toAppUserResponseDto()));
        return responseDtoList;
    }

    /**
     * 获取课程类型列表
     */
    public List<ClazzStyleResponseDto> clazzStyles() {
        List<ClazzStyle> all = clazzStyleRepository.findAll();
        return all.stream().map((ClazzStyle::toDto)).collect(Collectors.toList());
    }

    /**
     * 获取课程列表
     */
    public List<ClazzResponseDto> clazzList(Long clazzId) {
        Iterable<Course> all = courseRepository.findAll(course.styleId.eq(clazzId));
        if (all == null){
            return null;
        }
        List<Course> courses = ToListUtil.IterableToList(all);
        return courses.stream().map(Course::toClazzResponseDto).collect(Collectors.toList());
    }

    /**
     * 获取上级用户信息
     */
    public AppUserResponseDto parentInfo(Long userId) {
        TlongUser one = tlongUserRepository.findOne(tlongUser.id.eq(userId));
        if (Objects.nonNull(one)){
            if (one.getParentId() != null) {
                TlongUser one1 = tlongUserRepository.findOne(tlongUser.id.eq(one.getParentId()));
                if (Objects.nonNull(one1)) {
                    AppUserResponseDto responseDto = new AppUserResponseDto();
                    responseDto.setWx(one.getWx());
                    responseDto.setPhone(one.getPhone());
                    return responseDto;
                }
            }else {
                return new AppUserResponseDto();
            }
        }
        return new AppUserResponseDto();
    }
}

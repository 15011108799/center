package com.tlong.center.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.app.clazz.ClazzResponseDto;
import com.tlong.center.api.dto.app.clazz.ClazzStyleResponseDto;
import com.tlong.center.api.dto.app.user.AppUserLoginRequestDto;
import com.tlong.center.api.dto.app.user.AppUserLoginResponseDto;
import com.tlong.center.api.dto.app.user.AppUserResponseDto;
import com.tlong.center.api.exception.NosuchRecordException;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.course.ClazzStyle;
import com.tlong.center.domain.app.course.Course;
import com.tlong.center.domain.repository.ClazzStyleRepository;
import com.tlong.center.domain.repository.CourseRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
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

    private final Logger log  = LoggerFactory.getLogger(AppService.class);

    @Autowired
    private TlongUserRepository tlongUserRepository;

    @Autowired
    private ClazzStyleRepository clazzStyleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;


    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    /**
     * app登录业务
     */
    public AppUserLoginResponseDto appLogin(AppUserLoginRequestDto requestDto){
        TlongUser one = tlongUserRepository.findOne(tlongUser.userName.eq(requestDto.getUserName())
                .and(tlongUser.password.eq(requestDto.getPassword())));
        if (Objects.isNull(one)){
            return new AppUserLoginResponseDto(0,null);
        }
        return new AppUserLoginResponseDto(1,one.getId());
    }

    /**
     * 根据用户id获取用户基本信息
     * @param userId
     * @return
     */
    public AppUserResponseDto userInfo(Long userId) {
        TlongUser one = tlongUserRepository.findOne(userId);
        AppUserResponseDto responseDto = new AppUserResponseDto();
        if (Objects.isNull(one)){
           responseDto.setEvId(one.getEvId());
           responseDto.setHeadImage(one.getHeadImage());
           responseDto.setNickName(one.getNickName());
           responseDto.setServiceHotline(one.getServiceHotline());
           responseDto.setUserId(userId);
           responseDto.setWx(one.getWx());
        }
        responseDto.setUserId(0L);
        return responseDto;
    }

    /**
     * 根据用户id获取下级代理商信息
     * @param userId
     * @return
     */
    public List<AppUserResponseDto> children(Long userId) {
        Iterable<TlongUser> all = tlongUserRepository.findAll(tlongUser.parentId.eq(userId));
        List<AppUserResponseDto> responseDtoList = new ArrayList<>();
        all.forEach(one -> responseDtoList.add(one.toAppUserResponseDto()));
        return responseDtoList;
    }

    /**
     * 获取课程类型列表
     * @return
     */
    public List<ClazzStyleResponseDto> clazzStyles() {
        List<ClazzStyle> all = clazzStyleRepository.findAll();
        List<ClazzStyleResponseDto> collect = all.stream().map((one -> one.toDto())).collect(Collectors.toList());
        return collect;
    }

    /**
     * 获取课程列表
     * @param clazzId
     * @return
     */
    public List<ClazzResponseDto> clazzList(Long clazzId) {
        Iterable<Course> all = courseRepository.findAll(course.styleId.eq(clazzId));
        if (all == null){
            return null;
        }
        List<Course> courses = ToListUtil.IterableToList(all);
        return courses.stream().map(one -> one.toClazzResponseDto()).collect(Collectors.toList());
    }
}

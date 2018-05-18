package com.tlong.center.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.AppUserResponseDto;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Objects;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;


@Component
@Transactional
public class AppLoginService {

    private final Logger log  = LoggerFactory.getLogger(AppLoginService.class);

    @Autowired
    private TlongUserRepository tlongUserRepository;

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
    public AppUserResponseDto appLogin(AppUserRequestDto requestDto){

//        BooleanExpression eq = tlongUser.curState.eq(0);
//        queryFactory.select(tlongUser).from(tlongUser).where(tlongUser.id.eq(1L).and(eq));

//        List<String> fetch = queryFactory.selectFrom(tlongUser).select(tlongUser.userName).where(tlongUser.userName.eq(requestDto.getUserName()))
//                .fetch();

        TlongUser one = tlongUserRepository.findOne(tlongUser.userName.eq(requestDto.getUserName())
                .and(tlongUser.password.eq(requestDto.getPassword())));
        if (Objects.isNull(one)){
            return new AppUserResponseDto(0,null);
        }
        return new AppUserResponseDto(1,one.getId());
    }


    /**
     * 添加用户
     */
    public Long addUser(AppUserRequestDto requestDto) {
//        AppUser appUser = new AppUser();
//        appUser.setUserName(requestDto.getUserName());
//        appUser.setPassword(requestDto.getPassword());
//        return appUserRepository.save(appUser).getId();
        return null;
    }
}

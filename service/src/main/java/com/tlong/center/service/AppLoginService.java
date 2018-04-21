package com.tlong.center.service;

import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.AppUserResponseDto;
import com.tlong.center.domain.app.AppUser;
import com.tlong.center.domain.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.app.QAppUser.appUser;


@Component
@Transactional
public class AppLoginService {

    private final Logger log  = LoggerFactory.getLogger(AppLoginService.class);

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * app登录业务
     */
    public AppUserResponseDto appLogin(AppUserRequestDto requestDto){
        AppUser one = appUserRepository.findOne(appUser.userName.eq(requestDto.getUserName())
                .and(appUser.password.eq(requestDto.getPassword())));
        if (Objects.isNull(one)){
            return new AppUserResponseDto(0,null);
        }
        return new AppUserResponseDto(1,one.getId());
    }

    /**
     * 添加用户
     */
    public Long addUser(AppUserRequestDto requestDto) {
        AppUser appUser = new AppUser();
        appUser.setUserName(requestDto.getUserName());
        appUser.setPassword(requestDto.getPassword());
        return appUserRepository.save(appUser).getId();
    }
}

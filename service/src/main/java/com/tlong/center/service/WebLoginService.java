package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.TlongPowerDto;
import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongPowerRepository;
import com.tlong.center.domain.repository.TlongUserRepository;
import com.tlong.center.domain.web.TlongPower;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.web.QTlongUserRole.tlongUserRole;
import static com.tlong.center.domain.web.QTlongRole.tlongRole;
import static com.tlong.center.domain.web.QTlongRolePower.tlongRolePower;
import static com.tlong.center.domain.web.QTlongPower.tlongPower;

@Component
@Transactional
public class WebLoginService {

    private static Logger  logger = LoggerFactory.getLogger(WebLoginService.class);

    final EntityManager entityManager;
    final TlongUserRepository tlongUserRepository;
    final TlongPowerRepository tlongPowerRepository;

    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public WebLoginService(EntityManager entityManager, TlongUserRepository tlongUserRepository, TlongPowerRepository tlongPowerRepository) {
        this.entityManager = entityManager;
        this.tlongUserRepository = tlongUserRepository;
        this.tlongPowerRepository = tlongPowerRepository;
    }


    /**
     * 登录时自动验证账号名是否存在
     */
    public TlongResultDto checkUserName(String userName){
        Iterable<TlongUser> all = tlongUserRepository.findAll(tlongUser.userName.eq(userName));
        if (all == null){
            return new TlongResultDto(1,"用户名不存在");
        }else {
            return new TlongResultDto(0,"用户名正常");
        }
    }

    /**
     * 后台登录接口
     */
    public WebLoginResponseDto webLogin(WebLoginRequestDto requestDto) {

        //首先验证账号密码
//        String password = MD5Util.KL(MD5Util.MD5(requestDto.getPassword()));
        TlongUser findResult = tlongUserRepository.findOne(tlongUser.userName.eq(requestDto.getUserName())
                .and(tlongUser.password.eq(requestDto.getPassword())));
        if (Objects.nonNull(findResult)){
            logger.info("user"+ requestDto.getUserName() + "Login Success!");
        }else {
            return new WebLoginResponseDto(1,"账户名或密码不正确");
        }

        //获取权限列表

//        List<Tuple> tuples = queryFactory.select(tlongPower.id, tlongPower.powerName, tlongPower.powerLevel)
//                .from(tlongUser,tlongUserRole,tlongRole,tlongRolePower,tlongPower)
//                .leftJoin(tlongUserRole).on(tlongUser.id.eq(tlongUserRole.userId))
//                .leftJoin(tlongRole).on(tlongRole.id.eq(tlongUserRole.roleId))
//                .leftJoin(tlongRolePower).on(tlongRole.id.eq(tlongRolePower.roleId))
//                .leftJoin(tlongPower).on(tlongPower.id.eq(tlongRolePower.powerId))
//                .where(tlongUser.id.eq(findResult.getId()))
//                .fetch();

        List<Tuple> tuples = queryFactory.select(tlongPower.id, tlongPower.powerName, tlongPower.powerLevel)
                .from(tlongUser, tlongUserRole, tlongRole, tlongRolePower, tlongPower)
                .where(tlongUser.id.eq(tlongUserRole.userId)
                        .and(tlongRole.id.eq(tlongUserRole.roleId))
                        .and(tlongRole.id.eq(tlongRolePower.roleId))
                        .and(tlongPower.id.eq(tlongRolePower.powerId))
                        .and(tlongUser.id.eq(findResult.getId())))
                .fetch();

        WebLoginResponseDto webLoginResponseDto = new WebLoginResponseDto();
        List<TlongPowerDto> powerLevelOne = new ArrayList<>();
        List<TlongPowerDto> powerLevelTwo = new ArrayList<>();
        List<TlongPowerDto> powerLevelThree = new ArrayList<>();
        tuples.stream().forEach(one ->{
            TlongPowerDto dto = new TlongPowerDto(one.get(tlongPower.id), one.get(tlongPower.powerName), one.get(tlongPower.powerLevel));
            if (one.get(tlongPower.powerLevel) == 0){
                powerLevelOne.add(dto);
            }else if (one.get(tlongPower.powerLevel) == 1){
                powerLevelTwo.add(dto);
            }else if (one.get(tlongPower.powerLevel) == 2){
                powerLevelThree.add(dto);
            }
        });
        webLoginResponseDto.setPowersLevelOne(powerLevelOne);
        webLoginResponseDto.setPowersLevelTwo(powerLevelTwo);
        webLoginResponseDto.setPowersLevelThree(powerLevelThree);
        return webLoginResponseDto;
    }

}

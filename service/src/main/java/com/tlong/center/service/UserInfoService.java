package com.tlong.center.service;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.util.Objects;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class UserInfoService {

    private final TlongUserRepository tlongUserRepository;

    public UserInfoService(TlongUserRepository tlongUserRepository) {
        this.tlongUserRepository = tlongUserRepository;
    }

    /**
     * 校验用户名是否存在
     */
    public TlongResultDto checkUserName(String userName) {
        //用户名判重
        TlongUser tlongUser1 = tlongUserRepository.findOne(QTlongUser.tlongUser.userName.eq(userName));
        if (Objects.nonNull(tlongUser1)){
            return new TlongResultDto(0); //存在
        }else {
            return new TlongResultDto(1); //不存在
        }
    }

    /**
     * 修改用户密码
     */
    public TlongResultDto updateUserPassword(Long phone, String newPassword) {
        TlongUser one = tlongUserRepository.findOne(tlongUser.phone.eq(String.valueOf(phone)));
        if (Objects.nonNull(one)){
            String password = MD5Util.MD5(newPassword);
            one.setPassword(password);
            tlongUserRepository.save(one);
            return new TlongResultDto(0,"修改密码成功!");
        }else {
            return new TlongResultDto(1,"修改密码失败");
        }
    }

}

package com.tlong.center.service;

import com.tlong.center.api.dto.app.user.AppUserRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.common.code.CodeUtil;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.TlongUserRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
@Transactional
public class AppUserService {

    private final TlongUserRepository tlongUserRepository;
    private final CodeService codeService;

    public AppUserService(TlongUserRepository tlongUserRepository, CodeService codeService) {
        this.tlongUserRepository = tlongUserRepository;
        this.codeService = codeService;
    }

    /**
     * APP用户注册
     */
    public TlongResultDto addUser(AppUserRequestDto requestDto) {
        TlongUser tlongUser = new TlongUser();
        tlongUser.setUserName(requestDto.getUserName());
        tlongUser.setPassword(MD5Util.MD5(requestDto.getPassword()));
//        tlongUser.setRegistDate(new Date());
        tlongUser.setLevel(2);
        tlongUser.setPhone(requestDto.getPhone());
        tlongUser.setUserType(2);
        tlongUser.setOrgId(1426L);
        tlongUser.setIsCompany(0);

        //生成用户编码
        String userCode = codeService.createUserCode(2, 1, 0);
        tlongUser.setUserCode(userCode);
        TlongUser save = tlongUserRepository.save(tlongUser);
        return new TlongResultDto(0,"注册成功!");
    }
}

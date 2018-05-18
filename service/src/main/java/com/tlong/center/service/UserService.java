package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.user.UserSearchRequestDto;
import com.tlong.center.api.dto.user.UserSearchResponseDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * 供应商注册
     *
     * @param requsetDto
     * @return
     */
    public Result suppliersRegister(String path, SuppliersRegisterRequsetDto requsetDto) {
        TlongUser tlongUser = new TlongUser();
        if (requsetDto.getUserName() == null)
            return new Result(0,"用户名不能为空");
        tlongUser.setUserName(requsetDto.getUserName());
        if (requsetDto.getPassword() == null || requsetDto.getPassword().length() <= 6)
            return new Result(0,"密码格式不正确");
        tlongUser.setPassword(MD5Util.KL(MD5Util.MD5(requsetDto.getPassword())));
        tlongUser.setUserType(requsetDto.getUserType());
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setAge(requsetDto.getAge());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setPhone(requsetDto.getPhone());
        tlongUser.setHeadImage(path);
        tlongUser.setCreateDate(LocalDateTime.now());
        TlongUser user = appUserRepository.save(tlongUser);
        if (user == null) {
            return new Result(0,"注册失败");
        }
        return new Result(1,"注册成功");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public Integer deleteUserById(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        if (tlongUser == null)
            return 0;
        appUserRepository.delete(id);
        return 1;
    }


    /**
     * 查找用户
     *
     * @param requestDto
     * @return
     */
    public UserSearchResponseDto searchUser(UserSearchRequestDto requestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        if (requestDto.getpType() == 0)
            tlongUsers = appUserRepository.findAll(tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(1)).and(tlongUser.userName.like("%" + (requestDto.getUserName() == null ? "" : requestDto.getUserName()) + "%")), pageRequest);
        else
            tlongUsers = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getpType()).and(tlongUser.userName.like("%" + (requestDto.getUserName() == null ? "" : requestDto.getUserName()) + "%")), pageRequest);
        UserSearchResponseDto responseDto = new UserSearchResponseDto();
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setPassword(MD5Util.KL(MD5Util.MD5(tlongUser1.getPassword())));
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setPhone(tlongUser1.getPhone());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        responseDto.setSuppliersRegisterRequsetDtos(suppliersRegisterRequsetDtos);
        return responseDto;
    }

    /**
     * 用户是否认证
     *
     * @param id
     * @return
     */
    public Boolean authentication(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        return tlongUser != null && tlongUser.getEsgin() != null && 1 == tlongUser.getEsgin();
    }
}

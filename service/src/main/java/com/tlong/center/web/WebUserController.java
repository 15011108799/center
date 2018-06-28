package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.*;
import com.tlong.center.api.web.WebUserApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.xml.ws.http.HTTPBinding;
import java.util.List;

@RestController
@RequestMapping("/api/web/user")
public class WebUserController implements WebUserApi {


    @Autowired
    private UserService userService;

    @Override
    public Result suppliersRegister(@RequestBody SuppliersRegisterRequsetDto SuppliersRegisterRequsetDto) {
        SuppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(SuppliersRegisterRequsetDto.getHeadImage1()));
        return userService.suppliersRegister(SuppliersRegisterRequsetDto);
    }

    @Override
    public Long agentRegister(AgentRegisterRequestDto requestDto) {
        return null;
    }

    @Override
    public Integer deleteUser(@RequestBody Long id) {
        return userService.deleteUserById(id);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> searchUser(@RequestBody UserSearchRequestDto requestDto, HttpSession session) {
        return userService.searchUser(requestDto,session);
    }

    @Override
    public Boolean authentication(Long id) {
        return userService.authentication(id);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto, HttpSession session) {
        return userService.findAllSuppliers(requestDto,session);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllAgents(@RequestBody PageAndSortRequestDto requestDto,HttpSession session) {
        return userService.findAllAgents(requestDto,session);
    }

    @Override
    public SuppliersRegisterRequsetDto findSupplierById(@RequestBody Long id) {
        return userService.findOne(id);
    }

    @Override
    public Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto) {
        if (suppliersRegisterRequsetDto.getHeadImage1() != null&&!suppliersRegisterRequsetDto.getHeadImage1().equals(""))
            suppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getHeadImage1()));
        if (suppliersRegisterRequsetDto.getIdcardReverse1() != null)
            suppliersRegisterRequsetDto.setIdcardReverse1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardReverse1()));
        if (suppliersRegisterRequsetDto.getBusinessLicense1() != null)
            suppliersRegisterRequsetDto.setBusinessLicense1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getBusinessLicense1()));
        if (suppliersRegisterRequsetDto.getIdcardFront1() != null)
            suppliersRegisterRequsetDto.setIdcardFront1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardFront1()));
        return userService.update(suppliersRegisterRequsetDto);
    }

    @Override
    public void updateUserAuthentication(@RequestBody Long id) {
        userService.updateUserAuthentication(id);
    }

    @Override
    public Integer findUserPublishNumm( @RequestBody Long id) {
        return userService.findUserPublishNumm(id);
    }

    @Override
    public void updateUserPublishNumm(@RequestBody SuppliersRegisterRequsetDto registerRequsetDto) {
        userService.updateUserPublishNumm(registerRequsetDto);
    }

    @Override
    public Integer findCount(@RequestBody Integer type,HttpSession session) {
        return userService.findCount(type,session);
    }
}

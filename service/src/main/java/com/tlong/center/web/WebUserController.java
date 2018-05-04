package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.user.AgentRegisterRequestDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.user.UserSearchRequestDto;
import com.tlong.center.api.dto.user.UserSearchResponseDto;
import com.tlong.center.api.web.WebUserApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/web/user")
public class WebUserController implements WebUserApi {


    @Autowired
    private UserService userService;

    @Override
    public Result suppliersRegister(@RequestParam MultipartFile file, SuppliersRegisterRequsetDto requsetDto) {
        return userService.suppliersRegister(FileUploadUtils.upload(file), requsetDto);
    }

    @Override
    public Long agentRegister(AgentRegisterRequestDto requestDto) {
        return null;
    }

    @Override
    public Integer deleteUser(Long id) {
        return userService.deleteUserById(id);
    }

    @Override
    public UserSearchResponseDto searchUser(@RequestBody UserSearchRequestDto requestDto) {
        return userService.searchUser(requestDto);
    }

    @Override
    public Boolean authentication(Long id) {
        return userService.authentication(id);
    }
}

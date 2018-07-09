package com.tlong.center.app;

import com.tlong.center.api.app.AppUserApi;
import com.tlong.center.api.dto.app.clazz.ClazzResponseDto;
import com.tlong.center.api.dto.app.clazz.ClazzStyleResponseDto;
import com.tlong.center.api.dto.app.user.AppUserLoginRequestDto;
import com.tlong.center.api.dto.app.user.AppUserLoginResponseDto;
import com.tlong.center.api.dto.app.user.AppUserResponseDto;
import com.tlong.center.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/app/user")
public class AppUserController implements AppUserApi {

    @Autowired
    AppUserService appUserService;
    /**
     * App登录方法
     */
    @Override
    public AppUserLoginResponseDto appLogin(@RequestBody AppUserLoginRequestDto requestDto) {
        return appUserService.appLogin(requestDto);
    }

    /**
     * 根据用户id获取用户基本信息
     * @param userId
     * @return
     */
    @Override
    public AppUserResponseDto userInfo(@PathVariable Long userId) {
        return appUserService.userInfo(userId);
    }


    /**
     * 根据用户id获取下级代理商信息
     * @param userId
     * @return
     */
    @Override
    public List<AppUserResponseDto> children(@PathVariable Long userId) {
        return appUserService.children(userId);
    }


    /**
     * 获取课程类型列表
     * @return
     */
    @Override
    public List<ClazzStyleResponseDto> clazzStyles() {
        return appUserService.clazzStyles();
    }

    /**
     * 获取课程列表
     * @param clazzId
     * @return
     */
    @Override
    public List<ClazzResponseDto> clazzList(@PathVariable Long clazzId) {
        return appUserService.clazzList(clazzId);
    }


//    /**
//     * 添加用户
//     */
//    @PostMapping("/addUser")
//    public Long aaUser(@RequestBody AppUserRequestDto requestDto){
//        return appLoginService.addUser(requestDto);
//    }


}

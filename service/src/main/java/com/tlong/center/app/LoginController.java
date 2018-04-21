package com.tlong.center.app;

import com.tlong.center.api.app.AppLoginApi;
import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.AppUserResponseDto;
import com.tlong.center.service.AppLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/user")
public class LoginController implements AppLoginApi {

    @Autowired
    AppLoginService appLoginService;
    /**
     * App登录方法
     */
    @Override
    public AppUserResponseDto appLogin(@RequestBody AppUserRequestDto requestDto) {
        return appLoginService.appLogin(requestDto);
    }

    /**
     * 添加用户
     */
    @PostMapping("/addUser")
    public Long aaUser(@RequestBody AppUserRequestDto requestDto){
        return appLoginService.addUser(requestDto);
    }


}

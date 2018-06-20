package com.tlong.center.web;

import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;
import com.tlong.center.api.web.WebLoginApi;
import com.tlong.center.service.AppLoginService;
import com.tlong.center.service.WebLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/web/user")
public class WebLoginController implements WebLoginApi {

    @Autowired
    AppLoginService appLoginService;

    @Autowired
    WebLoginService webLoginService;

    /**
     * App登录方法
     */
    @Override
    public WebLoginResponseDto webLogin(@RequestBody WebLoginRequestDto requestDto, HttpSession session) {
        return webLoginService.webLogin(requestDto,session);
    }

    /**
     * 添加用户
     */
    @PostMapping("/addUser")
    public Long aaUser(@RequestBody AppUserRequestDto requestDto){
        return appLoginService.addUser(requestDto);
    }


}

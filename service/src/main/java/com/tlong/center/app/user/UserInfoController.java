package com.tlong.center.app.user;

import com.tlong.center.api.app.user.UserInfoApi;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/userInfo")
public class UserInfoController implements UserInfoApi {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 校验用户名是否存在
     */
    @Override
    public TlongResultDto checkUserName(@RequestParam String userName) {
        return userInfoService.checkUserName(userName);
    }

    /**
     * 修改用户密码
     */
    @Override
    public TlongResultDto updateUserPassword(@PathVariable Long phone, @RequestParam String newPassword) {
        return userInfoService.updateUserPassword(phone,newPassword);
    }
}

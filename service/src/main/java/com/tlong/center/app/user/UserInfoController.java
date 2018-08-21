package com.tlong.center.app.user;

import com.tlong.center.api.app.user.UserInfoApi;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.UserInfoService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

public class UserInfoController implements UserInfoApi {

    private final UserInfoService userInfoService;

    public UserInfoController(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    /**
     * 校验用户名是否存在
     */
    @Override
    public Integer checkUserName(@RequestParam String userName) {
        return userInfoService.checkUserName(userName);
    }

    /**
     * 修改用户密码
     */
    @Override
    public TlongResultDto updateUserPassword(@PathVariable Long userId, @RequestParam String newPassword) {
        return userInfoService.updateUserPassword(userId,newPassword);
    }
}

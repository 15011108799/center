package com.tlong.center.app.user;

import com.tlong.center.api.app.user.AppUserApi;
import com.tlong.center.api.dto.app.user.AppUserRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.AppUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/user/")
public class AppUserController implements AppUserApi {

   private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    /**
     * APP注册用户
     */
    @Override
    public TlongResultDto addUser(@RequestBody AppUserRequestDto requestDto) {
        return appUserService.addUser(requestDto);
    }
}

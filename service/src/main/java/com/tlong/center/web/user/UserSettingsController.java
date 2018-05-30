package com.tlong.center.web.user;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.settings.TlongUserSettingsRequestDto;
import com.tlong.center.api.web.user.UserSettingsApi;
import com.tlong.center.common.user.UserSettingsSerivce;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/user")
public class UserSettingsController implements UserSettingsApi {

    final UserSettingsSerivce settingsSerivce;

    public UserSettingsController(UserSettingsSerivce settingsSerivce) {
        this.settingsSerivce = settingsSerivce;
    }

    @Override
    public void updateGoodsReleaseNumber(@RequestBody TlongUserSettingsRequestDto req) {
        settingsSerivce.updateGoodsReleaseNumber(req);
    }
}

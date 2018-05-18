package com.tlong.center.app;

import com.tlong.center.api.app.AppUpdateApi;
import com.tlong.center.api.dto.app.AppUpdateCheckRequestDto;
import com.tlong.center.api.dto.app.AppUpdateCheckResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/appUpdate")
public class AppUpdateController implements AppUpdateApi {
    @Override
    public AppUpdateCheckResponseDto appUpdateCheck(@RequestBody AppUpdateCheckRequestDto requestDto) {
        return new AppUpdateCheckResponseDto(0,"###############");
    }
}

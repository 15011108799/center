package com.tlong.center.api.client;

import com.tlong.center.api.app.AppLoginApi;
import com.tlong.center.api.client.fallback.AppLoginClientFallback;
import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.AppUserResponseDto;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = AppLoginClientFallback.class)
public interface AppLoginClient extends AppLoginApi {
//    @Override
//    public AppUserResponseDto appLogin(AppUserRequestDto requestDto) {
//        return null;
//    }
}

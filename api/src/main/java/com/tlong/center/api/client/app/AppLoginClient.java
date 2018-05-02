package com.tlong.center.api.client.app;

import com.tlong.center.api.app.AppLoginApi;
import com.tlong.center.api.client.fallback.AppLoginClientFallback;
import com.tlong.center.api.dto.AppUserRequestDto;
import com.tlong.center.api.dto.AppUserResponseDto;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = AppLoginClientFallback.class)
public interface AppLoginClient extends AppLoginApi {
}

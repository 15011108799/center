package com.tlong.center.api.client.app;

import com.tlong.center.api.app.AppUserApi;
import com.tlong.center.api.client.fallback.AppLoginClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = AppLoginClientFallback.class)
public interface AppUserClient extends AppUserApi {
}

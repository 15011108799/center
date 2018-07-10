package com.tlong.center.api.client.app;

import com.tlong.center.api.app.AppApi;
import com.tlong.center.api.client.fallback.AppClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = AppClientFallback.class)
public interface AppUserClient extends AppApi {
}

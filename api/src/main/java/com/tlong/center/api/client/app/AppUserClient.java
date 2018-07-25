package com.tlong.center.api.client.app;

import com.tlong.center.api.app.AppBaseApi;
import com.tlong.center.api.client.fallback.AppBaseClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = AppBaseClientFallback.class)
public interface AppUserClient extends AppBaseApi {
}

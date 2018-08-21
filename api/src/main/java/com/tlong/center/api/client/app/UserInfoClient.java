package com.tlong.center.api.client.app;

import com.tlong.center.api.client.fallback.UserInfoClientFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = UserInfoClientFallBack.class)
public interface UserInfoClient {
}

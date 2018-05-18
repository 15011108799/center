package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.UserClientFallback;
import com.tlong.center.api.web.WebUserApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = UserClientFallback.class)
public interface WebUserClient extends WebUserApi{
}

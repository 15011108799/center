package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebPowerClientFallBack;
import com.tlong.center.api.web.WebPowerApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebPowerClientFallBack.class)
public interface WebPowerClient extends WebPowerApi {
}

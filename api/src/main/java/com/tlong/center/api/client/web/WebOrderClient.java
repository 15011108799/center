package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.OrderClientFallback;
import com.tlong.center.api.web.WebOrderApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot", path = "/api/web", fallback = OrderClientFallback.class)
public interface WebOrderClient extends WebOrderApi {
}

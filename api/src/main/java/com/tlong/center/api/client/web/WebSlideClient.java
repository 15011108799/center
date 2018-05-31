package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebSlideClientFallBack;
import com.tlong.center.api.web.WebSlideApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebSlideClientFallBack.class)
public interface WebSlideClient extends WebSlideApi {
}

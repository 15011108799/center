package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.MessageClientFallback;
import com.tlong.center.api.web.WebMessageApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = MessageClientFallback.class)
public interface WebMessageClient extends WebMessageApi {
}

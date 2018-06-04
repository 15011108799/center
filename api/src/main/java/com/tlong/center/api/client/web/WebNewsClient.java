package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.NewsClientFallback;
import com.tlong.center.api.web.WebNewsApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = NewsClientFallback.class)
public interface WebNewsClient extends WebNewsApi {
}

package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebPropertyClientFallBack;
import com.tlong.center.api.web.WebPropertyApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebPropertyClientFallBack.class)
public interface WebPropertyClient extends WebPropertyApi {
}

package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebRoleClientFallBack;
import com.tlong.center.api.web.WebRoleApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebRoleClientFallBack.class)
public interface WebRoleClient extends WebRoleApi {
}

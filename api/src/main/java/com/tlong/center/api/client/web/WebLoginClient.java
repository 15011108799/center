package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebLoginClientFallback;
import com.tlong.center.api.web.WebLoginApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "center",path = "/api/web",fallback = WebLoginClientFallback.class)
public interface WebLoginClient extends WebLoginApi{

}

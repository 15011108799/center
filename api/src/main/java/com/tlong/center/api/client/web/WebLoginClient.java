package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.AppLoginClientFallback;
import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;
import com.tlong.center.api.web.WebLoginApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = AppLoginClientFallback.class)
public interface WebLoginClient extends WebLoginApi{

}

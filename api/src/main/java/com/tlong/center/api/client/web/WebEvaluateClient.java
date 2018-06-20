package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.EvaluateClientFallback;
import com.tlong.center.api.web.WebEvaluateApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = EvaluateClientFallback.class)
public interface WebEvaluateClient extends WebEvaluateApi {
}

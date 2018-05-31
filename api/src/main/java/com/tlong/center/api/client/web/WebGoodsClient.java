package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebGoodsClientFallBack;
import com.tlong.center.api.web.WebGoodsApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot", path = "/api/web", fallback = WebGoodsClientFallBack.class)
public interface WebGoodsClient extends WebGoodsApi {
}

package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.WebGoodsRejectReasonClientFallBack;
import com.tlong.center.api.web.WebGoodsRejectReasonApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot", path = "/api/web", fallback = WebGoodsRejectReasonClientFallBack.class)
public interface WebGoodsRejectReasonClient extends WebGoodsRejectReasonApi {
}

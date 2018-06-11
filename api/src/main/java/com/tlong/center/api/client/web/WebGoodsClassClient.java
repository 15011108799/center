package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.GoodsClassClientFallback;
import com.tlong.center.api.web.WebGoodsClassApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = GoodsClassClientFallback.class)
public interface WebGoodsClassClient extends WebGoodsClassApi {
}

package com.tlong.center.api.client.app;

import com.tlong.center.api.app.EsignApi;
import com.tlong.center.api.client.fallback.EsignClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = EsignClientFallback.class)
public interface EsignClient extends EsignApi{
}

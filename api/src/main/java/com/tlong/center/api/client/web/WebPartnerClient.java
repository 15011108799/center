package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.PartnerClientFallback;
import com.tlong.center.api.web.WebPartnerApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = PartnerClientFallback.class)
public interface WebPartnerClient extends WebPartnerApi {
}

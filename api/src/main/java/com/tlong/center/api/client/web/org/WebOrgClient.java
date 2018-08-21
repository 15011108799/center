package com.tlong.center.api.client.web.org;

import com.tlong.center.api.client.fallback.WebOrgClientFallback;
import com.tlong.center.api.web.org.WebOrgApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebOrgClientFallback.class)
public interface WebOrgClient extends WebOrgApi {
}

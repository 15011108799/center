package com.tlong.center.api.client.app;

import com.tlong.center.api.client.fallback.IndexClientFallBack;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = IndexClientFallBack.class)
public interface InvitationClient {
}

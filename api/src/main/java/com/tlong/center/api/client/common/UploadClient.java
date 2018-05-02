package com.tlong.center.api.client.common;

import com.tlong.center.api.client.fallback.UploadClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/file",fallback = UploadClientFallback.class)
public interface UploadClient {
}

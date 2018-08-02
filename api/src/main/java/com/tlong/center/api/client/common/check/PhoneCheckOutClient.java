package com.tlong.center.api.client.common.check;

import com.tlong.center.api.client.fallback.UploadClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/common/check",fallback = UploadClientFallback.class)
public interface PhoneCheckOutClient {
}

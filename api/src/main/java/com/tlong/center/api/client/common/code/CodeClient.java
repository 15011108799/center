package com.tlong.center.api.client.common.code;

import com.tlong.center.api.client.fallback.CodeClientFallback;
import com.tlong.center.api.common.code.CodeApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = CodeClientFallback.class)
public interface CodeClient extends CodeApi {
}

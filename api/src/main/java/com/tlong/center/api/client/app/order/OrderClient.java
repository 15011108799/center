package com.tlong.center.api.client.app.order;

import com.tlong.center.api.app.order.OrderApi;
import com.tlong.center.api.client.fallback.OrderClientFallback;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/app",fallback = OrderClientFallback.class)
public interface OrderClient extends OrderApi {
}

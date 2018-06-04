package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class OrderClientFallback implements FallbackFactory<OrderClientFallback>{
    @Override
    public OrderClientFallback create(Throwable throwable) {
        return null;
    }
}

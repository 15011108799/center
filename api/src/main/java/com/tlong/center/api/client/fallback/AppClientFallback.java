package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class AppClientFallback implements FallbackFactory<AppClientFallback>{
    @Override
    public AppClientFallback create(Throwable throwable) {
        return null;
    }
}

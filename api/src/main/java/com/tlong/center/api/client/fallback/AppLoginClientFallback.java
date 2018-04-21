package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class AppLoginClientFallback implements FallbackFactory<AppLoginClientFallback>{
    @Override
    public AppLoginClientFallback create(Throwable throwable) {
        return null;
    }
}

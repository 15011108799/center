package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class AppBaseClientFallback implements FallbackFactory<AppBaseClientFallback>{
    @Override
    public AppBaseClientFallback create(Throwable throwable) {
        return null;
    }
}

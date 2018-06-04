package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class TlongClientFallback implements FallbackFactory<TlongClientFallback>{
    @Override
    public TlongClientFallback create(Throwable throwable) {
        return null;
    }
}

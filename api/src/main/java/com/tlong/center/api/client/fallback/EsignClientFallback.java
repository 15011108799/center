package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class EsignClientFallback implements FallbackFactory<EsignClientFallback> {
    @Override
    public EsignClientFallback create(Throwable throwable) {
        return null;
    }
}

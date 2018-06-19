package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class EvaluateClientFallback implements FallbackFactory<EvaluateClientFallback>{
    @Override
    public EvaluateClientFallback create(Throwable throwable) {
        return null;
    }
}

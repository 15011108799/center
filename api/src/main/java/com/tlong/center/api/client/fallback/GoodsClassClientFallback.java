package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class GoodsClassClientFallback implements FallbackFactory<GoodsClassClientFallback>{
    @Override
    public GoodsClassClientFallback create(Throwable throwable) {
        return null;
    }
}

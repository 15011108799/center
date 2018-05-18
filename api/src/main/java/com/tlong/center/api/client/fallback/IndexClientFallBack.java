package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class IndexClientFallBack implements FallbackFactory<IndexClientFallBack> {
    @Override
    public IndexClientFallBack create(Throwable throwable) {
        return null;
    }
}

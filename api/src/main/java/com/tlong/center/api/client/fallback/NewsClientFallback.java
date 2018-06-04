package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class NewsClientFallback implements FallbackFactory<NewsClientFallback>{
    @Override
    public NewsClientFallback create(Throwable throwable) {
        return null;
    }
}

package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

@Component
public class IndexClientFallBack implements FallbackFactory<IndexClientFallBack> {
    @Override
    public IndexClientFallBack create(Throwable throwable) {
        return null;
    }
}

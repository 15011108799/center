package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class CodeClientFallback implements FallbackFactory<CodeClientFallback> {
    @Override
    public CodeClientFallback create(Throwable throwable) {
        return null;
    }
}

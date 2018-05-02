package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class UploadClientFallback implements FallbackFactory<UploadClientFallback> {
    @Override
    public UploadClientFallback create(Throwable throwable) {
        return null;
    }
}

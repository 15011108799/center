package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebOrgClientFallback implements FallbackFactory<WebOrgClientFallback> {
    @Override
    public WebOrgClientFallback create(Throwable throwable) {
        return null;
    }
}

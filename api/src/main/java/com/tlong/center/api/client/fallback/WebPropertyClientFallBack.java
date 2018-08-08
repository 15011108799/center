package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebPropertyClientFallBack implements FallbackFactory<WebPropertyClientFallBack> {
    @Override
    public WebPropertyClientFallBack create(Throwable throwable) {
        return null;
    }
}

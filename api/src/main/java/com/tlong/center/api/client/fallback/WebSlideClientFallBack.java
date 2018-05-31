package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebSlideClientFallBack implements FallbackFactory<WebSlideClientFallBack> {
    @Override
    public WebSlideClientFallBack create(Throwable throwable) {
        return null;
    }
}

package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebPowerClientFallBack implements FallbackFactory<WebPowerClientFallBack> {
    @Override
    public WebPowerClientFallBack create(Throwable throwable) {
        return null;
    }
}

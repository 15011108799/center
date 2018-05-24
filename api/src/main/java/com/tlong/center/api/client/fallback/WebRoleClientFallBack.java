package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebRoleClientFallBack implements FallbackFactory<WebRoleClientFallBack> {
    @Override
    public WebRoleClientFallBack create(Throwable throwable) {
        return null;
    }
}

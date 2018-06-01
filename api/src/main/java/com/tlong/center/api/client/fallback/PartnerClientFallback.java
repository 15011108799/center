package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class PartnerClientFallback implements FallbackFactory<PartnerClientFallback>{
    @Override
    public PartnerClientFallback create(Throwable throwable) {
        return null;
    }
}

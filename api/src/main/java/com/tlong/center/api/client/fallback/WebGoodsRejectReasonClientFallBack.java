package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebGoodsRejectReasonClientFallBack implements FallbackFactory<WebGoodsRejectReasonClientFallBack> {
    @Override
    public WebGoodsRejectReasonClientFallBack create(Throwable throwable) {
        return null;
    }
}

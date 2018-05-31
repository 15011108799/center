package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebGoodsClientFallBack implements FallbackFactory<WebGoodsClientFallBack> {
    @Override
    public WebGoodsClientFallBack create(Throwable throwable) {
        return null;
    }
}

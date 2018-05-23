package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class MessageClientFallback implements FallbackFactory<MessageClientFallback>{
    @Override
    public MessageClientFallback create(Throwable throwable) {
        return null;
    }
}

package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class UserClientFallback  implements FallbackFactory<UserClientFallback>{
    @Override
    public UserClientFallback create(Throwable throwable) {
        return null;
    }
}

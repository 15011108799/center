package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class UserInfoClientFallBack implements FallbackFactory<UserInfoClientFallBack> {
    @Override
    public UserInfoClientFallBack create(Throwable throwable) {
        return null;
    }
}

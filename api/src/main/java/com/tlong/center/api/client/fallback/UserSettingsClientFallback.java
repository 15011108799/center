package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class UserSettingsClientFallback implements FallbackFactory<UserSettingsClientFallback> {
    @Override
    public UserSettingsClientFallback create(Throwable throwable) {
        return null;
    }
}

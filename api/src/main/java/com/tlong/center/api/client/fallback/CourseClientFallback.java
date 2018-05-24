package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class CourseClientFallback implements FallbackFactory<CourseClientFallback>{
    @Override
    public CourseClientFallback create(Throwable throwable) {
        return null;
    }
}

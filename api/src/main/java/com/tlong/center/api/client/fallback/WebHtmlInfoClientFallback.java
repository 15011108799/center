package com.tlong.center.api.client.fallback;

import feign.hystrix.FallbackFactory;

public class WebHtmlInfoClientFallback implements FallbackFactory<WebHtmlInfoClientFallback> {
    @Override
    public WebHtmlInfoClientFallback create(Throwable throwable) {
        return null;
    }
}

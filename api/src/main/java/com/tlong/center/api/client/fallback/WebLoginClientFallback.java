package com.tlong.center.api.client.fallback;

import com.tlong.center.api.client.web.WebLoginClient;
import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;
import feign.hystrix.FallbackFactory;

import javax.servlet.http.HttpSession;

public class WebLoginClientFallback implements FallbackFactory<WebLoginClientFallback> {
    @Override
    public WebLoginClientFallback create(Throwable throwable) {
        return null;
    }
}

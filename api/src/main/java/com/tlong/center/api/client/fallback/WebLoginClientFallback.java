package com.tlong.center.api.client.fallback;

import com.tlong.center.api.client.web.WebLoginClient;
import com.tlong.center.api.dto.web.WebLoginRequestDto;
import com.tlong.center.api.dto.web.WebLoginResponseDto;

public class WebLoginClientFallback implements WebLoginClient {
    @Override
    public WebLoginResponseDto webLogin(WebLoginRequestDto requestDto) {
        return null;
    }
}

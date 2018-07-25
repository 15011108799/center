package com.tlong.center.api.client.web;

import com.tlong.center.api.client.fallback.AppBaseClientFallback;
import com.tlong.center.api.web.WebHtmlInfoApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = AppBaseClientFallback.class)
public interface WebHtmlInfoClient extends WebHtmlInfoApi{
}

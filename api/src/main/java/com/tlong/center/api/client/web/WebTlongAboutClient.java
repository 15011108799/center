package com.tlong.center.api.client.web;

import com.tlong.center.api.web.WebTlongAboutApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = WebTlongAboutClient.class)
public interface WebTlongAboutClient extends WebTlongAboutApi {
}

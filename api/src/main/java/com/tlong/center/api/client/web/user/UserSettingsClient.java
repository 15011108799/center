package com.tlong.center.api.client.web.user;

import com.tlong.center.api.client.fallback.CourseClientFallback;
import com.tlong.center.api.client.fallback.UserSettingsClientFallback;
import com.tlong.center.api.web.user.UserSettingsApi;
import org.springframework.cloud.netflix.feign.FeignClient;

@FeignClient(value = "boot",path = "/api/web",fallback = UserSettingsClientFallback.class)
public interface UserSettingsClient extends UserSettingsApi {
}

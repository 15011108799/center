package com.tlong.center.app.org;

import com.tlong.center.api.app.org.OrgApi;
import com.tlong.center.api.dto.app.org.AppOrgBaseInfoResponseDto;
import com.tlong.center.service.AppOrgService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/app/org")
public class OrgController implements OrgApi {

    private final AppOrgService appOrgService;

    public OrgController(AppOrgService appOrgService) {
        this.appOrgService = appOrgService;
    }


    /**
     * 获取上级机构基本信息
     */
    @Override
    public AppOrgBaseInfoResponseDto findParentOrgInfo(@PathVariable Long orgId) {
        return appOrgService.findParentOrgInfo(orgId);
    }
}

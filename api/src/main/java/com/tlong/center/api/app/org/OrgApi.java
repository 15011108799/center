package com.tlong.center.api.app.org;

import com.tlong.center.api.dto.app.org.AppOrgBaseInfoResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Api("获取父级机构信息")
public interface OrgApi {

    @ApiOperation("父级机构信息")
    @PostMapping("/{orgId}")
    AppOrgBaseInfoResponseDto findParentOrgInfo(@PathVariable Long orgId);
}

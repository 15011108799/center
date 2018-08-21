package com.tlong.center.api.web.org;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.org.AddOrgRequestDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("机构管理")
public interface WebOrgApi {

    @ApiOperation("新增机构")
    @PostMapping("")
    TlongResultDto addOrg(@RequestBody AddOrgRequestDto requestDto);
}

package com.tlong.center.api.web.org;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.org.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("机构管理")
public interface WebOrgApi {

    @ApiOperation("新增机构")
    @PostMapping("")
    TlongResultDto addOrg(@RequestBody AddOrgRequestDto requestDto);

    @ApiOperation("查询省 市 区(县)机构列表 供应商分公司列表")
    @PostMapping("/orgList")
    Page<SuppliersCompanyResponseDto> orgList(SuppliersCompanyRequestDto requestDto);

    @ApiOperation("获取下级代理商信息列表")
    @PostMapping("/agentList/{orgId}")
    Page<TlongOrgResponseDto> agentList(@PathVariable Long orgId, @RequestBody PageAndSortRequestDto pageAndSortRequestDto);

    @ApiModelProperty
    @PostMapping("/findOne/{orgId}")
    TlongOrgResponseDto findOne(@PathVariable Long orgId);

    @ApiModelProperty
    @PostMapping("/updateOne")
    TlongResultDto updateOne(@RequestBody UpdateOrgRequestDto requestDto);
    //根据id获取单个部门信息  修改接口
}

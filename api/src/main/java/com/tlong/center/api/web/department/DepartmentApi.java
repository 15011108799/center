package com.tlong.center.api.web.department;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.department.AddDepartmentRequestDto;
import com.tlong.center.api.dto.web.department.DepartmentInfoResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Api("部门相关接口")
public interface DepartmentApi {

    @ApiOperation("新增部门")
    @PostMapping("")
    TlongResultDto addDepartment(@RequestBody AddDepartmentRequestDto requestDto);

    @ApiOperation("获取部门分页列表")
    @PostMapping("/departmentPage/{orgId}")
    Page<DepartmentInfoResponseDto> departmentPage(@PathVariable Long orgId, @RequestBody PageAndSortRequestDto requestDto);


    @ApiOperation("部门权限对应绑定")
    @PostMapping("/addRolePower/{roleId}/{powerIds}")
    TlongResultDto addRolePower(@PathVariable Long roleId, @PathVariable String powerIds);
}

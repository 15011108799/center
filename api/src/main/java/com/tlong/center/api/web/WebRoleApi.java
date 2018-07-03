package com.tlong.center.api.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebRoleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;


@Api("角色增删改查接口")
public interface WebRoleApi {

    @ApiOperation("获取角色列表")
    @PostMapping("/roleList")
    PageResponseDto<WebRoleDto> roleList(@RequestBody PageAndSortRequestDto requestDto);

    @ApiModelProperty("新增角色")
    @PostMapping("/addRole")
    Integer addRole(@RequestBody WebRoleDto reqDto);

    @ApiModelProperty("删除角色")
    @PutMapping("/delRole")
    TlongResultDto delRole(@RequestParam Long roleId);

    @ApiModelProperty("修改角色信息")
    @PutMapping("/updateRole/{roleId}")
    TlongResultDto updateRole(@RequestBody WebRoleDto reqDto, @PathVariable Long roleId);

    @ApiModelProperty("角色绑定权限")
    @GetMapping("/bindPower/{roleId}/{powerIds}")
    void bindPower(@PathVariable Long roleId, @PathVariable String powerIds);

}

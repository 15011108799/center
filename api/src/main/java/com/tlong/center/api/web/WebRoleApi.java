package com.tlong.center.api.web;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.WebRoleDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Api("角色增删改查接口")
public interface WebRoleApi {

    //TODO 前端修改
    @ApiOperation("获取其他部门角色列表")
    @PostMapping("/roleList")
    Page<WebRoleDto> roleList(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("获取所有角色列表")
    @PostMapping("/allRoleList")
    List<WebRoleDto> allRoleList();

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

    @ApiModelProperty("角色修改权限")
    @GetMapping("/updatePower/{roleId}/{powerIds}")
    void updatePower(@PathVariable Long roleId, @PathVariable String powerIds);

}

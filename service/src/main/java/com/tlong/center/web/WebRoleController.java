package com.tlong.center.web;

import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.WebRoleDto;
import com.tlong.center.api.web.WebRoleApi;
import com.tlong.center.service.WebRoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/web/role")
public class WebRoleController implements WebRoleApi {


    final WebRoleService webRoleService;

    public WebRoleController(WebRoleService webRoleService) {
        this.webRoleService = webRoleService;
    }

    /**
     * 角色列表查询
     */
    @Override
    public List<WebRoleDto> roleList() {
        return webRoleService.roleList();
    }

    /**
     * 新增角色
     */
    @Override
    public TlongResultDto addRole(@RequestBody WebRoleDto reqDto) {
        return webRoleService.addRole(reqDto);
    }

    /**
     * 删除角色
     */
    @Override
    public TlongResultDto delRole(@RequestParam Long roleId) {
        return webRoleService.delRole(roleId);
    }

    /**
     * 修改角色
     */
    @Override
    public TlongResultDto updateRole(@RequestBody WebRoleDto reqDto, @PathVariable Long roleId) {
        return webRoleService.updateRole(reqDto,roleId);
    }
}

package com.tlong.center.web.department;

import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.web.department.AddDepartmentRequestDto;
import com.tlong.center.api.dto.web.department.DepartmentInfoResponseDto;
import com.tlong.center.api.web.department.DepartmentApi;
import com.tlong.center.service.DepartmentService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/web/department")
public class DepartmentController implements DepartmentApi {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 新增部门
     */
    @Override
    public TlongResultDto addDepartment(@RequestBody AddDepartmentRequestDto requestDto) {
        return departmentService.addDepartment(requestDto);
    }

    /**
     * 获取部门下所有机构列表分页
     */
    @Override
    public Page<DepartmentInfoResponseDto> departmentPage(@PathVariable Long orgId, @RequestBody PageAndSortRequestDto requestDto) {
        return departmentService.departmentPage(orgId,requestDto);
    }

    /**
     * 绑定部门权限
     */
    @Override
    public TlongResultDto addRolePower(@PathVariable Long roleId,@PathVariable String powerIds) {
        return departmentService.addRolePower(roleId,powerIds);
    }
}

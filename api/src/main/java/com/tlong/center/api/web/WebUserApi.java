package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.AgentResponseDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.web.FindUserPublishNumResponseDto;
import com.tlong.center.api.dto.web.UpdateUserPublishNumRequsetDto;
import com.tlong.center.api.dto.web.user.AddManagerRequestDto;
import com.tlong.center.api.dto.web.user.OrgManagerInfoResponseDto;
import com.tlong.center.api.dto.web.user.TlongUserResponseDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Api("用户管理接口")
public interface WebUserApi {

    @ApiModelProperty("创建管理员用户")
    @PostMapping("/createManager")
    TlongResultDto createManager(@RequestBody AddManagerRequestDto requestDto);

    @ApiOperation("获取下级代理商分页")
    @PostMapping("/childrenAgents")
    Page<AgentResponseDto> childrenAgents(@RequestParam Long userId, @RequestBody PageAndSortRequestDto pageAndSortRequestDto);

    @ApiOperation("用户注册接口")
    @PostMapping("/suppliersRegister")
    Result suppliersRegister(@RequestBody SuppliersRegisterRequsetDto SuppliersRegisterRequsetDto);

    @ApiOperation("用户删除(代理商丶供货商)")
    @PutMapping("/deleteUser")
    Integer deleteUser(@RequestParam Long id);

    @ApiOperation("用户认证(供应商代理商)")
    @PutMapping("/authentication")
    Boolean authentication(@RequestParam Long id);

    @ApiOperation("查询所有供货商")
    @PostMapping("/findAllSuppliers/{userId}")
    Page<TlongUserResponseDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long userId, @RequestParam MultiValueMap<String,String> params);

    @ApiOperation("查询所有用户代理商")
    @PostMapping("/findAllAgents/{userId}")
    Page<TlongUserResponseDto> findAllAgents(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long userId, @RequestParam MultiValueMap<String,String> params);

    @ApiOperation("根据id查询用户")
    @PutMapping("/findSupplierById")
    SuppliersRegisterRequsetDto findSupplierById(@RequestParam Long id);

    @ApiOperation("修改用户信息")
    @PutMapping("/updateUserInfo")
    Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto);

    @ApiOperation("修改供应商状态")
    @PostMapping("/updateUserAuthentication")
    void updateUserAuthentication(@RequestBody Long id);

    @ApiOperation("查找供应商发布商品数量")
    @PostMapping("/findUserPublishNum")
    FindUserPublishNumResponseDto findUserPublishNumm(@RequestParam Long id, @RequestParam Integer isCompany);

    @ApiOperation("修改供应商发布商品数量")
    @PostMapping("/updateUserPublishNum")
    void updateUserPublishNumm(@RequestBody UpdateUserPublishNumRequsetDto requsetDto);

    @ApiOperation("查询所有高级管理员")
    @PostMapping("/findAllManager")
    Page<SuppliersRegisterRequsetDto> findAllManager(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("查询某个部门管理员")
    @PostMapping("/findOrgManager/{orgId}")
    Page<OrgManagerInfoResponseDto> findOrgManager(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long orgId);

    @ApiOperation("删除总公司超级合伙人管理员")
    @GetMapping("/delManage/{id}/{roleId}")
    void delManage(@PathVariable Long id, @PathVariable Long roleId);

//    @ApiOperation("查找供应商认证通过人数")
//    @PostMapping("/findCount")
//    Integer findCount(@RequestBody Integer type, HttpSession session);
//
//    @ApiOperation("搜索查找供应商认证通过人数")
//    @PostMapping("/findCount1")
//    Integer findCount(@RequestBody UserSearchRequestDto requestDto, HttpSession session);

//    @ApiOperation("查询所有供货商分公司")
//    @PostMapping("/findSupplirtCompany")
//    Page<SuppliersCompanyResponseDto> findSupplirtCompany(@RequestBody PageAndSortRequestDto requestDto);
//    @ApiOperation("用户搜索代理商分公司层级搜索")
//    @PostMapping("/searchAgentByLevel")
//    PageResponseDto<SuppliersRegisterRequsetDto> searchAgentByLevel(@RequestBody UserSearchRequestDto requestDto);
//    @ApiOperation("查询同一层级所有代理商")
//    @PostMapping("/findAgentByLevel")
//    Page<SuppliersCompanyResponseDto> findAgentByLevel(@RequestBody SuppliersCompanyRequestDto requestDto);
    //    @ApiOperation("用户搜索(代理商丶供货商)")
//    @PostMapping("/searchUser")
//    Page<SuppliersRegisterRequsetDto> searchUser(@RequestBody UserSearchRequestDto requestDto, @RequestParam MultiValueMap<String,String> params);
    //    @ApiOperation("代理商注册接口")
//    @PostMapping("/agentRegister")
//    Long agentRegister(@RequestBody AgentRegisterRequestDto requestDto);
//    @ApiOperation("搜索供货商分公司")
//    @PostMapping("/searchSupplirtCompany")
//    PageResponseDto<SuppliersRegisterRequsetDto> searchSupplirtCompany(@RequestBody UserSearchRequestDto requestDto);
//
//    @ApiOperation("查询某公司所有供货商")
//    @PostMapping("/findSupplierByOrg")
//    PageResponseDto<SuppliersRegisterRequsetDto> findSupplierByOrg(@RequestBody PageAndSortRequestDto requestDto);
//
//    @ApiOperation("用户搜索某公司所有供货商")
//    @PostMapping("/searchSupplierByOrg")
//    PageResponseDto<SuppliersRegisterRequsetDto> searchSupplierByOrg(@RequestBody UserSearchRequestDto requestDto);
}

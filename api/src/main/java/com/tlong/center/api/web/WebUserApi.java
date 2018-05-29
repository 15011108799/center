package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Api("代理商供应商用户管理接口")
public interface WebUserApi {

    @ApiOperation("供应商注册接口")
    @PostMapping("/suppliersRegister")
    //TODO
    Result suppliersRegister(@RequestBody SuppliersRegisterRequsetDto SuppliersRegisterRequsetDto);

    @ApiOperation("代理商注册接口")
    @PostMapping("/agentRegister")
    //TODO
    Long agentRegister(@RequestBody AgentRegisterRequestDto requestDto);


    @ApiOperation("用户删除(代理商丶供货商)")
    @PutMapping("/deleteUser")
    Integer deleteUser(@RequestParam Long id);


//    @ApiOperation("用户修改(代理商丶供货商)")
//    @PutMapping("/updateUser")
      //TODO 可能接口要分开写
//    Integer updateUser(@RequestBody )

    @ApiOperation("用户搜索(代理商丶供货商)")
    @PostMapping("/searchUser")
    //TODO
    UserSearchResponseDto searchUser(@RequestBody UserSearchRequestDto requestDto);


    @ApiOperation("用户认证(供应商代理商)")
    @PutMapping("/authentication")
    Boolean authentication(@RequestParam Long id);

    @ApiOperation("查询所有用户供货商")
    @PostMapping("/findAllSuppliers")
        //TODO
    PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto);

    @ApiOperation("查询所有用户代理商")
    @PostMapping("/findAllAgents")
        //TODO
    List<SuppliersRegisterRequsetDto> findAllAgents();

    @ApiOperation("根据id查询用户")
    @PutMapping("/findSupplierById")
        //TODO
    SuppliersRegisterRequsetDto findSupplierById(@RequestParam Long id);

    @ApiOperation("修改用户信息")
    @PutMapping("/updateUserInfo")
        //TODO
    Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto);
    //TODO 代理商下级信息拉取
}

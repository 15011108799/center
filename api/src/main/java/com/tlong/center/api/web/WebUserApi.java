package com.tlong.center.api.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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
    PageResponseDto<SuppliersRegisterRequsetDto> searchUser(@RequestBody UserSearchRequestDto requestDto,HttpSession session);


    @ApiOperation("用户认证(供应商代理商)")
    @PutMapping("/authentication")
    Boolean authentication(@RequestParam Long id);

    @ApiOperation("查询所有用户供货商")
    @PostMapping("/findAllSuppliers")
    PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto, HttpSession session);

    @ApiOperation("查询所有用户代理商")
    @PostMapping("/findAllAgents")
    PageResponseDto<SuppliersRegisterRequsetDto> findAllAgents(@RequestBody PageAndSortRequestDto requestDto,HttpSession session);

    @ApiOperation("根据id查询用户")
    @PutMapping("/findSupplierById")
    SuppliersRegisterRequsetDto findSupplierById(@RequestParam Long id);

    @ApiOperation("修改用户信息")
    @PutMapping("/updateUserInfo")
    Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto);
    //TODO 代理商下级信息拉取

    @ApiOperation("修改供应商状态")
    @PostMapping("/updateUserAuthentication")
    void updateUserAuthentication(@RequestBody Long id);

    @ApiOperation("查找供应商发布商品数量")
    @PostMapping("/findUserPublishNum")
    Integer findUserPublishNumm(@RequestBody Long id);

    @ApiOperation("修改供应商发布商品数量")
    @PostMapping("/updateUserPublishNum")
    void updateUserPublishNumm(@RequestBody SuppliersRegisterRequsetDto registerRequsetDto);

    @ApiOperation("查找供应商认证通过人数")
    @PostMapping("/findCount")
    Integer findCount(@RequestBody Integer type,HttpSession session);

    @ApiOperation("搜索查找供应商认证通过人数")
    @PostMapping("/findCount1")
    Integer findCount(@RequestBody UserSearchRequestDto requestDto,HttpSession session);
}

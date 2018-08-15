package com.tlong.center.web;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.*;
import com.tlong.center.api.web.WebUserApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/web/user")
public class WebUserController implements WebUserApi {


    private final UserService userService;

    public WebUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取下级代理商分页查询
     */
    @Override
    public Page<AgentResponseDto> childrenAgents(@RequestParam Long userId, @RequestBody PageAndSortRequestDto pageAndSortRequestDto) {
        return userService.childrenAgents(userId,pageAndSortRequestDto);
    }

    @Override
    public Result suppliersRegister(@RequestBody SuppliersRegisterRequsetDto SuppliersRegisterRequsetDto) {
        SuppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(SuppliersRegisterRequsetDto.getHeadImage()));
        return userService.suppliersRegister(SuppliersRegisterRequsetDto);
    }

    @Override
    public Long agentRegister(AgentRegisterRequestDto requestDto) {
        return null;
    }

    @Override
    public Integer deleteUser(@RequestBody Long id) {
        return userService.deleteUserById(id);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> searchUser(@RequestBody UserSearchRequestDto requestDto, HttpSession session) {
        return userService.searchUser(requestDto,session);
    }

    @Override
    public Boolean authentication(Long id) {
        return userService.authentication(id);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto, HttpSession session) {
        return userService.findAllSuppliers(requestDto,session);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllAgents(@RequestBody PageAndSortRequestDto requestDto,HttpSession session) {
        return userService.findAllAgents(requestDto,session);
    }

    @Override
    public SuppliersRegisterRequsetDto findSupplierById(@RequestBody Long id) {
        return userService.findOne(id);
    }

    @Override
    public Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto) {
        if (suppliersRegisterRequsetDto.getHeadImage() != null&&!suppliersRegisterRequsetDto.getHeadImage().equals(""))
            suppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getHeadImage()));
        if (suppliersRegisterRequsetDto.getIdcardReverse1() != null)
            suppliersRegisterRequsetDto.setIdcardReverse1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardReverse1()));
        if (suppliersRegisterRequsetDto.getBusinessLicense1() != null)
            suppliersRegisterRequsetDto.setBusinessLicense1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getBusinessLicense1()));
        if (suppliersRegisterRequsetDto.getIdcardFront1() != null)
            suppliersRegisterRequsetDto.setIdcardFront1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardFront1()));
        return userService.update(suppliersRegisterRequsetDto);
    }

    @Override
    public void updateUserAuthentication(@RequestBody Long id) {
        userService.updateUserAuthentication(id);
    }

    @Override
    public Integer findUserPublishNumm( @RequestBody Long id) {
        return userService.findUserPublishNumm(id);
    }

    @Override
    public void updateUserPublishNumm(@RequestBody SuppliersRegisterRequsetDto registerRequsetDto) {
        userService.updateUserPublishNumm(registerRequsetDto);
    }

    @Override
    public Integer findCount(@RequestBody Integer type,HttpSession session) {
        return userService.findCount(type,session);
    }

    @Override
    public Integer findCount(@RequestBody UserSearchRequestDto requestDto, HttpSession session) {
        return userService.findCount1(requestDto,session);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAgentByLevel(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findAgentByLevel(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplirtCompany(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findSupplirtCompany(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllManager(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findAllManager(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findOrgManager(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findOrgManager(requestDto);
    }

    @Override
    public void delManage(@PathVariable Long id,@PathVariable  Long roleId) {
        userService.delManage(id,roleId);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> searchAgentByLevel(@RequestBody UserSearchRequestDto requestDto) {
        return userService.searchAgentByLevel(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplirtCompany(@RequestBody UserSearchRequestDto requestDto) {
        return userService.searchSupplirtCompany(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplierByOrg(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findSupplierByOrg(requestDto);
    }

    @Override
    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplierByOrg(@RequestBody UserSearchRequestDto requestDto) {
        return userService.searchSupplierByOrg(requestDto);
    }
}

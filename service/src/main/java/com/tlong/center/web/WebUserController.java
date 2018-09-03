package com.tlong.center.web;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.*;
import com.tlong.center.api.dto.web.FindUserPublishNumResponseDto;
import com.tlong.center.api.dto.web.UpdateUserPublishNumRequsetDto;
import com.tlong.center.api.dto.web.org.SuppliersCompanyRequestDto;
import com.tlong.center.api.dto.web.org.SuppliersCompanyResponseDto;
import com.tlong.center.api.dto.web.user.AddManagerRequestDto;
import com.tlong.center.api.dto.web.user.OrgManagerInfoResponseDto;
import com.tlong.center.api.dto.web.user.TlongUserResponseDto;
import com.tlong.center.api.web.WebUserApi;
import com.tlong.center.common.utils.FileUploadUtils;
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/web/user")
public class WebUserController implements WebUserApi {


    private final UserService userService;

    public WebUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 创建管理员用户
     */
    @Override
    public TlongResultDto createManager(@RequestBody AddManagerRequestDto requestDto) {
        return userService.createManager(requestDto);
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
//        SuppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(SuppliersRegisterRequsetDto.getHeadImage()));
        return userService.suppliersRegister(SuppliersRegisterRequsetDto);
    }

    @Override
    public Integer deleteUser(@RequestBody Long id) {
        return userService.deleteUserById(id);
    }

//    @Override
//    public Page<SuppliersRegisterRequsetDto> searchUser(@RequestBody UserSearchRequestDto requestDto, @RequestParam MultiValueMap<String,String> params) {
//        return userService.searchUser(requestDto,params);
//    }

    @Override
    public Boolean authentication(Long id) {
        return userService.authentication(id);
    }


    /**
     * 查询所有供应商
     */
    @Override
    public Page<TlongUserResponseDto> findAllSuppliers(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long userId, @RequestParam MultiValueMap<String,String> params) {
        return userService.findAllSuppliers(requestDto,userId,params);
    }

    /**
     * 查询所有代理商
     */
    @Override
    public Page<TlongUserResponseDto> findAllAgents(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long userId, @RequestParam MultiValueMap<String,String> params) {
        return userService.findAllAgents(requestDto,userId,params);
    }

    @Override
    public SuppliersRegisterRequsetDto findSupplierById(@RequestBody Long id) {
        return userService.findOne(id);
    }

    @Override
    public Result updateUserInfo(@RequestBody SuppliersRegisterRequsetDto suppliersRegisterRequsetDto) {
//        if (suppliersRegisterRequsetDto.getHeadImage() != null&&!suppliersRegisterRequsetDto.getHeadImage().equals(""))
//            suppliersRegisterRequsetDto.setHeadImage1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getHeadImage()));
//        if (suppliersRegisterRequsetDto.getIdcardReverse1() != null)
//            suppliersRegisterRequsetDto.setIdcardReverse1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardReverse1()));
//        if (suppliersRegisterRequsetDto.getBusinessLicense1() != null)
//            suppliersRegisterRequsetDto.setBusinessLicense1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getBusinessLicense1()));
//        if (suppliersRegisterRequsetDto.getIdcardFront1() != null)
//            suppliersRegisterRequsetDto.setIdcardFront1(FileUploadUtils.readFile(suppliersRegisterRequsetDto.getIdcardFront1()));
        return userService.update(suppliersRegisterRequsetDto);
    }

    @Override
    public void updateUserAuthentication(@RequestBody Long id) {
        userService.updateUserAuthentication(id);
    }

    @Override
    public FindUserPublishNumResponseDto findUserPublishNumm(@RequestParam Long id, @RequestParam Integer isCompany) {
        return userService.findUserPublishNumm(id,isCompany);
    }

    @Override
    public void updateUserPublishNumm(@RequestBody UpdateUserPublishNumRequsetDto requsetDto) {
        userService.updateUserPublishNumm(requsetDto);
    }


//
//    @Override
//    public Page<SuppliersCompanyResponseDto> findAgentByLevel(@RequestBody SuppliersCompanyRequestDto requestDto) {
//        return userService.findAgentByLevel(requestDto);
//    }

    @Override
    public Page<SuppliersRegisterRequsetDto> findAllManager(@RequestBody PageAndSortRequestDto requestDto) {
        return userService.findAllManager(requestDto);
    }

    @Override
    public Page<OrgManagerInfoResponseDto> findOrgManager(@RequestBody PageAndSortRequestDto requestDto, @PathVariable Long orgId) {
        return userService.findOrgManager(requestDto,orgId);
    }

    @Override
    public void delManage(@PathVariable Long id,@PathVariable  Long roleId) {
        userService.delManage(id,roleId);
    }

//    @Override
//    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplirtCompany(@RequestBody UserSearchRequestDto requestDto) {
//        return userService.searchSupplirtCompany(requestDto);
//    }
//
//    @Override
//    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplierByOrg(@RequestBody PageAndSortRequestDto requestDto) {
//        return userService.findSupplierByOrg(requestDto);
//    }
//
//    @Override
//    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplierByOrg(@RequestBody UserSearchRequestDto requestDto) {
//        return userService.searchSupplierByOrg(requestDto);
//    }


    /**
     * 用户多条件模糊查询
     */
    public Predicate[] resove(MultiValueMap<String,String> params) {

        String userType = params.getFirst("userType");
        String checkState = params.getFirst("checkState");
        String esignState = params.getFirst("esignState");
        String userName = params.getFirst("userName");
        String userCode = params.getFirst("userCode");
        String realName = params.getFirst("realName");
        String beginTime = params.getFirst("beginTime");
        String endTime = params.getFirst("endTime");


        BooleanExpression userTypeEq = StringUtils.isNotBlank(userType) ? QTlongUser.tlongUser.userType.eq(Integer.valueOf(userType)) : null;
        BooleanExpression checkStateEq = StringUtils.isNotBlank(checkState) ? QTlongUser.tlongUser.authentication.eq(Integer.valueOf(checkState)) : null;
        BooleanExpression esignStateEq = StringUtils.isNotBlank(esignState) ? QTlongUser.tlongUser.esgin.eq(Integer.valueOf(esignState)) : null;
        BooleanExpression userNameLike = StringUtils.isNotBlank(userName) ? QTlongUser.tlongUser.userName.like("%" + userName + "%") : null;
        BooleanExpression userCodeLike = StringUtils.isNotBlank(userCode) ? QTlongUser.tlongUser.userCode.like("%" + userCode + "%") : null;
        BooleanExpression realNameLike = StringUtils.isNotBlank(realName) ? QTlongUser.tlongUser.realName.like("%" + realName + "%") : null;
//        BooleanExpression beforeEndTime = StringUtils.isNotBlank(endTime) ? QTlongUser.tlongUser.newstime.lt(Long.valueOf(endTime)) : null;
//        BooleanExpression AfterBeginTime = StringUtils.isNotBlank(endTime) ? QWebGoods.webGoods.newstime.gt(Long.valueOf(beginTime)) : null;

        List<BooleanExpression> list = new ArrayList<>();
        list.add(userTypeEq);
        list.add(checkStateEq);
        list.add(esignStateEq);
        list.add(userNameLike);
        list.add(userCodeLike);
        list.add(realNameLike);
//        list.add(beforeEndTime);
//        list.add(AfterBeginTime);

        List<BooleanExpression> collect = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Predicate[] pre = {QWebGoods.webGoods.id.isNotNull()};
        collect.forEach(one -> pre[0] = ExpressionUtils.and(pre[0], one));
        return pre;
    }


    //    @Override
//    public Integer findCount(@RequestBody Integer type,HttpSession session) {
//        return userService.findCount(type,session);
//    }
//
//    @Override
//    public Integer findCount(@RequestBody UserSearchRequestDto requestDto, HttpSession session) {
//        return userService.findCount1(requestDto,session);
//    }

//    @Override
//    public Page<SuppliersCompanyResponseDto> findSupplirtCompany(@RequestBody PageAndSortRequestDto requestDto) {
//        return userService.findSupplirtCompany(requestDto);
//    }

//    @Override
//    public PageResponseDto<SuppliersRegisterRequsetDto> searchAgentByLevel(@RequestBody UserSearchRequestDto requestDto) {
//        return userService.searchAgentByLevel(requestDto);
//    }
}

package com.tlong.center.service;

import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.user.UserSearchRequestDto;
import com.tlong.center.api.dto.user.UserSearchResponseDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;

    /**
     * 供应商注册
     *
     * @param requsetDto
     * @return
     */
    public Result suppliersRegister(SuppliersRegisterRequsetDto requsetDto) {
        TlongUser tlongUser = new TlongUser();
        if (requsetDto.getUserName() == null)
            return new Result(0, "用户名不能为空");
        tlongUser.setUserName(requsetDto.getUserName());
        if (requsetDto.getPassword() == null || requsetDto.getPassword().length() <= 6)
            return new Result(0, "密码格式不正确");
        tlongUser.setPassword(MD5Util.KL(MD5Util.MD5(requsetDto.getPassword())));
        tlongUser.setUserType(requsetDto.getUserType());
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setAge(requsetDto.getAge());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setHeadImage(requsetDto.getHeadImage1());
        tlongUser.setCreateDate(LocalDateTime.now());
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        TlongUser user = appUserRepository.save(tlongUser);
        if (user == null) {
            return new Result(0, "注册失败");
        }
        return new Result(1, "注册成功");
    }

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public Integer deleteUserById(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        if (tlongUser == null)
            return 0;
        appUserRepository.delete(id);
        return 1;
    }


    /**
     * 查找用户
     *
     * @param requestDto
     * @return
     */
    public UserSearchResponseDto searchUser(UserSearchRequestDto requestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        if (requestDto.getpType() == 0)
            tlongUsers = appUserRepository.findAll(tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(1)).and(tlongUser.userName.like("%" + (requestDto.getUserName() == null ? "" : requestDto.getUserName()) + "%")), pageRequest);
        else
            tlongUsers = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getpType()).and(tlongUser.userName.like("%" + (requestDto.getUserName() == null ? "" : requestDto.getUserName()) + "%")), pageRequest);
        UserSearchResponseDto responseDto = new UserSearchResponseDto();
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setPassword(MD5Util.KL(MD5Util.MD5(tlongUser1.getPassword())));
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        responseDto.setSuppliersRegisterRequsetDtos(suppliersRegisterRequsetDtos);
        return responseDto;
    }

    /**
     * 用户是否认证
     *
     * @param id
     * @return
     */
    public Boolean authentication(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        return tlongUser != null && tlongUser.getEsgin() != null && 1 == tlongUser.getEsgin();
    }

    /**
     * 查询所有供应商
     *
     * @return
     */
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto=new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1),pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(tlongUser1.getId());
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setNickName(tlongUser1.getNickName());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1));
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    public SuppliersRegisterRequsetDto findOne(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
        registerRequsetDto.setUserName(tlongUser.getUserName());
        registerRequsetDto.setPassword(tlongUser.getPassword());
        registerRequsetDto.setUserType(tlongUser.getUserType());
        registerRequsetDto.setIsCompany(tlongUser.getIsCompany());
        registerRequsetDto.setRealName(tlongUser.getRealName());
        registerRequsetDto.setAge(tlongUser.getAge());
        registerRequsetDto.setSex(tlongUser.getSex());
        registerRequsetDto.setHeadImage1(tlongUser.getHeadImage());
        registerRequsetDto.setIdcardFront1(tlongUser.getIdcardFront());
        registerRequsetDto.setPhone(tlongUser.getPhone());
        registerRequsetDto.setIdcardReverse1(tlongUser.getIdcardReverse());
        registerRequsetDto.setOrganizationCode(tlongUser.getOrganizationCode());
        registerRequsetDto.setSucc(tlongUser.getSucc());
        registerRequsetDto.setIdcardNumber(tlongUser.getIdcardNumber());
        registerRequsetDto.setLegalPersonName(tlongUser.getLegalPersonName());
        registerRequsetDto.setBusinessLicense1(tlongUser.getBusinessLicense());
        registerRequsetDto.setWx(tlongUser.getWx());
        registerRequsetDto.setServiceHotline(tlongUser.getServiceHotline());
        registerRequsetDto.setPremises(tlongUser.getPremises());
        registerRequsetDto.setOrgId(tlongUser.getOrgId());
        registerRequsetDto.setNickName(tlongUser.getNickName());
        registerRequsetDto.setIsExemption(tlongUser.getIsExemption());
        return registerRequsetDto;
    }

    public Result update(SuppliersRegisterRequsetDto requsetDto) {
        TlongUser tlongUser = new TlongUser();
        tlongUser.setId(requsetDto.getId());
        tlongUser.setUserName(requsetDto.getUserName());
        tlongUser.setPassword(requsetDto.getPassword());
        tlongUser.setUserType(requsetDto.getUserType());
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setIdcardNumber(requsetDto.getIdcardNumber());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setAge(requsetDto.getAge());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        TlongUser tlongUser1=appUserRepository.findOne(requsetDto.getId());
        if (requsetDto.getHeadImage1()!=null&& !requsetDto.getHeadImage1().equals("")) {
            tlongUser.setHeadImage(requsetDto.getHeadImage1());
        }else {
            tlongUser.setHeadImage(tlongUser1.getHeadImage());
        }
        if (requsetDto.getIdcardFront1()!=null&&!requsetDto.getIdcardFront1() .equals("")) {
            tlongUser.setIdcardFront(requsetDto.getIdcardFront1());
        }else
            tlongUser.setIdcardFront(tlongUser1.getIdcardFront());
        tlongUser.setPhone(requsetDto.getPhone());
        if (!requsetDto.getIdcardReverse1() .equals(""))
            tlongUser.setIdcardReverse(requsetDto.getIdcardReverse1());
        else
            tlongUser.setIdcardReverse(tlongUser1.getIdcardReverse());
        tlongUser.setOrganizationCode(requsetDto.getOrganizationCode());
        tlongUser.setSucc(requsetDto.getSucc());
        tlongUser.setLegalPersonName(requsetDto.getLegalPersonName());
        if (!requsetDto.getBusinessLicense1() .equals(""))
            tlongUser.setBusinessLicense(requsetDto.getBusinessLicense1());
        else
            tlongUser.setBusinessLicense(tlongUser1.getBusinessLicense());
        tlongUser.setCreateDate(LocalDateTime.now());
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        TlongUser tlongUser2 = appUserRepository.save(tlongUser);
        if (tlongUser2 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");

    }

    public List<SuppliersRegisterRequsetDto> findAllAgents() {
        Iterable<TlongUser> tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3).or(tlongUser.userType.intValue().eq(4))));
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(tlongUser1.getId());
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setOrgId(tlongUser1.getOrgId());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setNickName(tlongUser1.getNickName());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        return suppliersRegisterRequsetDtos;
    }
}

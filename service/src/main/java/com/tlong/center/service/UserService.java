package com.tlong.center.service;

import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.user.SuppliersRegisterRequsetDto;
import com.tlong.center.api.dto.user.UserSearchRequestDto;
import com.tlong.center.api.dto.user.UserSearchResponseDto;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.common.user.QTlongUserSettings;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.TlongUserSettingsRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;

@Component
@Transactional
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TlongUserSettingsRepository settingsRepository;

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

        //TODO
        if (requsetDto.getPassword() == null || requsetDto.getPassword().length() <= 6)
            return new Result(0, "密码格式不正确");
        tlongUser.setPassword(MD5Util.KL(MD5Util.MD5(requsetDto.getPassword())));
        tlongUser.setUserType(requsetDto.getUserType());
        if (requsetDto.getUserType() == 1) {
            tlongUser.setGoodsPublishNum(settingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.intValue().eq(0)).getGoodsReleaseNumber());
        }
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setAge(requsetDto.getAge());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setHeadImage(requsetDto.getHeadImage1());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        tlongUser.setRegistDate(simpleDateFormat.format(new Date()));
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
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
    public PageResponseDto<SuppliersRegisterRequsetDto> searchUser(UserSearchRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        Predicate pre = tlongUser.id.isNotNull();
        if (requestDto.getPtype() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
        else if (requestDto.getPtype() == 2)
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(2));
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (StringUtils.isNotEmpty(requestDto.getUserCode()))
            pre = ExpressionUtils.and(pre, tlongUser.userCode.eq(requestDto.getUserCode()));
        if (requestDto.getEsign() == 0 || requestDto.getEsign() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(requestDto.getEsign()));
        if (requestDto.getAuthentication() == 0 || requestDto.getAuthentication() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(requestDto.getAuthentication()));
        tlongUsers = appUserRepository.findAll(pre, pageRequest);
        UserSearchResponseDto responseDto = new UserSearchResponseDto();
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(tlongUser1.getId());
            registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setNickName(tlongUser1.getNickName());
            registerRequsetDto.setEsgin(tlongUser1.getEsgin());
            registerRequsetDto.setAuthentication(tlongUser1.getAuthentication());
            registerRequsetDto.setOrgId(tlongUser1.getOrgId());
            registerRequsetDto.setPremises(tlongUser1.getPremises());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        responseDto.setSuppliersRegisterRequsetDtos(suppliersRegisterRequsetDtos);
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(pre);
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
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
    public PageResponseDto<SuppliersRegisterRequsetDto> findAllSuppliers(PageAndSortRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        Page<TlongUser> tlongUser2;
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        if (user.getIsCompany() == 0)
            tlongUser2 = appUserRepository.findAll(tlongUser.id.longValue().eq(user.getId()), pageRequest);
        else
            tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.orgId.eq(user.getOrgId())), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(tlongUser1.getId());
            registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setAge(tlongUser1.getAge());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setNickName(tlongUser1.getNickName());
            registerRequsetDto.setEsgin(tlongUser1.getEsgin());
            registerRequsetDto.setAuthentication(tlongUser1.getAuthentication());
            registerRequsetDto.setOrgId(tlongUser1.getOrgId());
            registerRequsetDto.setPremises(tlongUser1.getPremises());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3;
        if (user.getIsCompany() == 0)
            tlongUser3 = appUserRepository.findAll(tlongUser.id.longValue().eq(user.getId()));
        else
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
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
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        TlongUser tlongUser1 = appUserRepository.findOne(requsetDto.getId());
        if (requsetDto.getHeadImage1() != null && !requsetDto.getHeadImage1().equals("")) {
            tlongUser.setHeadImage(requsetDto.getHeadImage1());
        } else {
            tlongUser.setHeadImage(tlongUser1.getHeadImage());
        }
        if (requsetDto.getIdcardFront1() != null && !requsetDto.getIdcardFront1().equals("")) {
            tlongUser.setIdcardFront(requsetDto.getIdcardFront1());
        } else
            tlongUser.setIdcardFront(tlongUser1.getIdcardFront());
        tlongUser.setPhone(requsetDto.getPhone());
        if (!requsetDto.getIdcardReverse1().equals(""))
            tlongUser.setIdcardReverse(requsetDto.getIdcardReverse1());
        else
            tlongUser.setIdcardReverse(tlongUser1.getIdcardReverse());
        tlongUser.setOrganizationCode(requsetDto.getOrganizationCode());
        tlongUser.setSucc(requsetDto.getSucc());
        tlongUser.setLegalPersonName(requsetDto.getLegalPersonName());
        if (!requsetDto.getBusinessLicense1().equals(""))
            tlongUser.setBusinessLicense(requsetDto.getBusinessLicense1());
        else
            tlongUser.setBusinessLicense(tlongUser1.getBusinessLicense());
        tlongUser.setRegistDate(tlongUser1.getRegistDate());
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

    public PageResponseDto<SuppliersRegisterRequsetDto> findAllAgents(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(2), pageRequest);
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
            registerRequsetDto.setEsgin(tlongUser1.getEsgin());
            registerRequsetDto.setAuthentication(tlongUser1.getAuthentication());
            registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(2));
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    /**
     * 修改认证状态
     *
     * @param id
     */
    public void updateUserAuthentication(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        if (tlongUser.getAuthentication() == 0)
            tlongUser.setAuthentication(1);
        else
            tlongUser.setAuthentication(0);
        appUserRepository.save(tlongUser);
    }

    /**
     * 查询发布商品数量
     *
     * @param id
     */
    public Integer findUserPublishNumm(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        return tlongUser.getGoodsPublishNum();
    }

    /**
     * 修改发布商品数量
     *
     * @param registerRequsetDto
     */
    public void updateUserPublishNumm(SuppliersRegisterRequsetDto registerRequsetDto) {
        TlongUser tlongUser = appUserRepository.findOne(registerRequsetDto.getId());
        tlongUser.setGoodsPublishNum(registerRequsetDto.getGoodsPublishNum());
        appUserRepository.save(tlongUser);
    }

    /**
     * 查询 认证通过的人数
     */
    public Integer findCount(int type) {
        Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(type).and(tlongUser.esgin.intValue().eq(1).and(tlongUser.authentication.intValue().eq(1))));
        final int[] count = {0};
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        return count[0];
    }
}

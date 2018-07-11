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
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.common.user.QTlongUserSettings;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.TlongRoleRepository;
import com.tlong.center.domain.repository.TlongUserRoleRepository;
import com.tlong.center.domain.repository.TlongUserSettingsRepository;
import com.tlong.center.domain.web.QTlongRole;
import com.tlong.center.domain.web.QTlongUserRole;
import com.tlong.center.domain.web.TlongUserRole;
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
import static com.tlong.center.domain.web.QTlongUserRole.tlongUserRole;
import static com.tlong.center.domain.web.QWebOrder.webOrder;

@Component
@Transactional
public class UserService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private TlongUserRoleRepository tlongUserRoleRepository;
    @Autowired
    private TlongUserSettingsRepository settingsRepository;
    @Autowired
    private TlongRoleRepository tlongRoleRepository;
    @Autowired
    private CodeService codeService;

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
        /* tlongUser.setPassword(MD5Util.KL(MD5Util.MD5(requsetDto.getPassword())));*/
        tlongUser.setPassword(requsetDto.getPassword());
        tlongUser.setUserType(requsetDto.getUserType());
        if (requsetDto.getUserType() != null && requsetDto.getUserType() == 1) {
            tlongUser.setGoodsPublishNum(settingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.intValue().eq(0)).getGoodsReleaseNumber());
            if (requsetDto.getIsCompany() == 0)
                tlongUser.setUserCode(codeService.createAllCode(3, 0, 1));
            else
                tlongUser.setUserCode(codeService.createAllCode(3, 1, 1));
        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 2) {
            tlongUser.setUserCode(codeService.createAllCode(2, 0, 1));
        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 3) {
            tlongUser.setUserCode(codeService.createAllCode(2, 1, 1));
        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 4) {
            tlongUser.setUserCode(codeService.createAllCode(2, 2, 1));
        }
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setBirthday(requsetDto.getBirthday());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setArea(requsetDto.getArea());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setHeadImage(requsetDto.getHeadImage1());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        tlongUser.setRegistDate(simpleDateFormat.format(new Date()));
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setPhone(requsetDto.getPhone());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
        TlongUser user = appUserRepository.save(tlongUser);
        if (requsetDto.getRoleId() != null) {
            TlongUserRole tlongUserRole = new TlongUserRole();
            tlongUserRole.setRoleId(requsetDto.getRoleId());
            tlongUserRole.setUserId(user.getId());
            tlongUserRoleRepository.save(tlongUserRole);
        }
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
    public PageResponseDto<SuppliersRegisterRequsetDto> searchUser(UserSearchRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        Predicate pre = tlongUser.id.isNotNull();
        if (requestDto.getPtype() != null) {
            if (requestDto.getPtype() != 1 && requestDto.getPtype() != 5 && user.getOrgId() != null) {
                pre = ExpressionUtils.and(pre, tlongUser.userType.isNotNull());
                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getPtype()));
                pre = ExpressionUtils.and(pre, tlongUser.orgId.like(user.getOrgId() + "%"));
                pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
            } else if (requestDto.getPtype() == 5) {
                if (user.getOrgId() != null) {
                    pre = ExpressionUtils.and(pre, tlongUser.orgId.like(user.getOrgId() + "%"));
                    pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
                }
                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)));
            } else if (requestDto.getPtype() == 1) {
                if (user.getIsCompany() == null)
                    pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
                else if (user.getIsCompany() == 0)
                    pre = ExpressionUtils.and(pre, tlongUser.id.longValue().eq(user.getId()));
                else {
                    pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
                    pre = ExpressionUtils.and(pre, tlongUser.isCompany.intValue().eq(0));
                    pre = ExpressionUtils.and(pre, tlongUser.orgId.eq(user.getOrgId()));
                }
            } else {
                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getPtype()));
            }
        }
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (StringUtils.isNotEmpty(requestDto.getUserCode()))
            pre = ExpressionUtils.and(pre, tlongUser.userCode.eq(requestDto.getUserCode()));
        if (requestDto.getEsign() == 0 || requestDto.getEsign() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(requestDto.getEsign()));
        if (requestDto.getAuthentication() == 0 || requestDto.getAuthentication() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(requestDto.getAuthentication()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
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
            registerRequsetDto.setBirthday(tlongUser1.getBirthday());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setUserCode(tlongUser1.getUserCode());
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
        if (user.getIsCompany() == null)
            tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)), pageRequest);
        else if (user.getIsCompany() == 0 || user.getIsCompany() == 1)
            tlongUser2 = appUserRepository.findAll(tlongUser.id.longValue().eq(user.getId()), pageRequest);
        else
            tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.eq(user.getOrgId())), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(tlongUser1.getId());
            registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
            registerRequsetDto.setUserName(tlongUser1.getUserName());
            registerRequsetDto.setUserType(tlongUser1.getUserType());
            registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
            registerRequsetDto.setRealName(tlongUser1.getRealName());
            registerRequsetDto.setBirthday(tlongUser1.getBirthday());
            registerRequsetDto.setSex(tlongUser1.getSex());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setNickName(tlongUser1.getNickName());
            registerRequsetDto.setEsgin(tlongUser1.getEsgin());
            registerRequsetDto.setAuthentication(tlongUser1.getAuthentication());
            registerRequsetDto.setOrgId(tlongUser1.getOrgId());
            registerRequsetDto.setPremises(tlongUser1.getPremises());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setUserCode(tlongUser1.getUserCode());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3;
        if (user.getIsCompany() == null)
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)));
        else if (user.getIsCompany() == 0)
            tlongUser3 = appUserRepository.findAll(tlongUser.id.longValue().eq(user.getId()));
        else
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
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
        registerRequsetDto.setBirthday(tlongUser.getBirthday());
        registerRequsetDto.setSex(tlongUser.getSex());
        registerRequsetDto.setArea(tlongUser.getArea());
        registerRequsetDto.setCompanyName(tlongUser.getCompanyName());
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
        registerRequsetDto.setUserCode(tlongUser.getUserCode());
        return registerRequsetDto;
    }

    public Result update(SuppliersRegisterRequsetDto requsetDto) {
        TlongUser tlongUser = new TlongUser();
        tlongUser.setId(requsetDto.getId());
        tlongUser.setArea(requsetDto.getArea());
        tlongUser.setUserName(requsetDto.getUserName());
        tlongUser.setPassword(requsetDto.getPassword());
        tlongUser.setUserType(requsetDto.getUserType());
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setIdcardNumber(requsetDto.getIdcardNumber());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setBirthday(requsetDto.getBirthday());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setCompanyName(requsetDto.getCompanyName());
        TlongUser tlongUser1 = appUserRepository.findOne(requsetDto.getId());
        tlongUser.setUserCode(tlongUser1.getUserCode());
        tlongUser.setPid(tlongUser1.getPid());
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
        if (requsetDto.getIdcardReverse1() != null && !requsetDto.getIdcardReverse1().equals(""))
            tlongUser.setIdcardReverse(requsetDto.getIdcardReverse1());
        else
            tlongUser.setIdcardReverse(tlongUser1.getIdcardReverse());
        tlongUser.setOrganizationCode(requsetDto.getOrganizationCode());
        tlongUser.setSucc(requsetDto.getSucc());
        tlongUser.setLegalPersonName(requsetDto.getLegalPersonName());
        if (requsetDto.getBusinessLicense1() != null && !requsetDto.getBusinessLicense1().equals(""))
            tlongUser.setBusinessLicense(requsetDto.getBusinessLicense1());
        else
            tlongUser.setBusinessLicense(tlongUser1.getBusinessLicense());
        tlongUser.setRegistDate(tlongUser1.getRegistDate());
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setOrgId(requsetDto.getOrgId());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        tlongUser.setPhone(requsetDto.getPhone());
        TlongUser tlongUser2 = appUserRepository.save(tlongUser);
        if (tlongUser2 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");

    }

    public PageResponseDto<SuppliersRegisterRequsetDto> findAllAgents(PageAndSortRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUser2;
        if (user.getUserType() == null) {
            if (requestDto.getPid() == null)
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")), pageRequest);
            else
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")).and(tlongUser.pid.longValue().eq(requestDto.getPid())), pageRequest);
        } else {
            if (requestDto.getPid() == null) {
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")).and(tlongUser.id.longValue().ne(user.getId())), pageRequest);
            } else {
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.pid.longValue().eq(requestDto.getPid()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")).and(tlongUser.id.longValue().ne(user.getId()))), pageRequest);
            }
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            if (user.getOrgId() == null || user.getOrgId().split("-").length == 3 || user.getOrgId().split("-").length - tlongUser1.getOrgId().split("-").length == -1) {
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setId(tlongUser1.getId());
                registerRequsetDto.setUserName(tlongUser1.getUserName());
                registerRequsetDto.setUserType(tlongUser1.getUserType());
                registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
                registerRequsetDto.setOrgId(tlongUser1.getOrgId());
                registerRequsetDto.setRealName(tlongUser1.getRealName());
                registerRequsetDto.setBirthday(tlongUser1.getBirthday());
                registerRequsetDto.setSex(tlongUser1.getSex());
                registerRequsetDto.setWx(tlongUser1.getWx());
                registerRequsetDto.setNickName(tlongUser1.getNickName());
                registerRequsetDto.setEsgin(tlongUser1.getEsgin());
                registerRequsetDto.setAuthentication(tlongUser1.getAuthentication());
                registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
                registerRequsetDto.setUserCode(tlongUser1.getUserCode());
                suppliersRegisterRequsetDtos.add(registerRequsetDto);
            }
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3;
        if (user.getUserType() == null) {
            if (requestDto.getPid() == null)
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()));
            else
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.pid.longValue().eq(requestDto.getPid())));
        } else {
            if (requestDto.getPid() == null) {
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")).and(tlongUser.id.longValue().ne(user.getId())));
            } else {
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.pid.longValue().eq(requestDto.getPid()).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.like(user.getOrgId() + "%")).and(tlongUser.id.longValue().ne(user.getId()))));
            }
        }
        tlongUser3.forEach(tlongUser1 -> {
            if (user.getOrgId() == null || user.getOrgId().split("-").length == 3 || user.getOrgId().split("-").length - tlongUser1.getOrgId().split("-").length == -1 || requestDto.getPid() != null) {
                count[0]++;
            }
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
    public Integer findCount(int type, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        Iterable<TlongUser> tlongUser3;
        if (type != 5)
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(type).and(tlongUser.esgin.intValue().eq(1).and(tlongUser.authentication.intValue().eq(1))));
        else
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)).and(tlongUser.esgin.intValue().eq(1).and(tlongUser.authentication.intValue().eq(1))));
        final int[] count = {0};
        tlongUser3.forEach(tlongUser1 -> {
            if (user.getUserType() != null && user.getUserType() != 1) {
                if (user.getOrgId() == null)
                    count[0]++;
                else {
                    if (user.getOrgId().split("-").length == 3 || user.getOrgId().split("-").length - tlongUser1.getOrgId().split("-").length == -1) {
                        count[0]++;
                    }
                }
            } else
                count[0]++;
        });
        return count[0];
    }

    public Integer findCount1(UserSearchRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        Iterable<TlongUser> tlongUser3;
        Predicate pre = tlongUser.id.isNotNull();
        if (requestDto.getPtype() != 1 && requestDto.getPtype() != 5 && user.getOrgId() != null) {
            pre = ExpressionUtils.and(pre, tlongUser.userType.isNotNull());
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getPtype()));
            pre = ExpressionUtils.and(pre, tlongUser.orgId.like(user.getOrgId() + "%"));
            pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
        } else if (requestDto.getPtype() == 5) {
            if (user.getOrgId() != null) {
                pre = ExpressionUtils.and(pre, tlongUser.orgId.like(user.getOrgId() + "%"));
                pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
            }
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)));
        } else if (requestDto.getPtype() == 1) {
            if (user.getIsCompany() == null)
                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
            else if (user.getIsCompany() == 0)
                pre = ExpressionUtils.and(pre, tlongUser.id.longValue().eq(user.getId()));
            else {
                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
                pre = ExpressionUtils.and(pre, tlongUser.isCompany.intValue().eq(0));
                pre = ExpressionUtils.and(pre, tlongUser.orgId.eq(user.getOrgId()));
            }
        } else {
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getPtype()));
        }
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (StringUtils.isNotEmpty(requestDto.getUserCode()))
            pre = ExpressionUtils.and(pre, tlongUser.userCode.eq(requestDto.getUserCode()));
        if (requestDto.getEsign() == 0 || requestDto.getEsign() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(requestDto.getEsign()));
        if (requestDto.getAuthentication() == 0 || requestDto.getAuthentication() == 1)
            pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(requestDto.getAuthentication()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(1));
        pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(1));
        tlongUser3 = appUserRepository.findAll(pre);
        final int[] count = {0};
        tlongUser3.forEach(tlongUser1 -> {
            if (user.getUserType() != null && user.getUserType() != 1) {
                if (user.getOrgId() == null)
                    count[0]++;
                else {
                    if (user.getOrgId().split("-").length == 3 || user.getOrgId().split("-").length - tlongUser1.getOrgId().split("-").length == -1) {
                        count[0]++;
                    }
                }
            } else
                count[0]++;
        });
        return count[0];
    }

    /**
     * 查找同一等级的代理商
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<SuppliersRegisterRequsetDto> findAgentByLevel(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUserRole> tlongUserRoles;
        if (requestDto.getLevel() == 0) {
            tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(11), pageRequest);
        } else if (requestDto.getLevel() == 1) {
            tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(10), pageRequest);
        } else
            tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(7), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUserRoles.forEach(one -> {
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                registerRequsetDto.setUserName(tlongUser.getUserName());
                registerRequsetDto.setId(tlongUser.getId());
                registerRequsetDto.setPassword(tlongUser.getPassword());
                registerRequsetDto.setPhone(tlongUser.getPhone());
                registerRequsetDto.setWx(tlongUser.getWx());
                registerRequsetDto.setArea(tlongUser.getArea());
                registerRequsetDto.setOrgId(tlongUser.getOrgId());
                registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
                suppliersRegisterRequsetDtos.add(registerRequsetDto);
            } else {
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(one.getUserId())
                        .and(QTlongUser.tlongUser.orgId.eq(requestDto.getOrg())));
                if (tlongUser != null) {
                    Iterable<TlongUser> tlongUsers=appUserRepository.findAll(QTlongUser.tlongUser.orgId.like( tlongUser.getUserName().substring(tlongUser.getUserName().indexOf('-')+1,tlongUser.getUserName().length())+"%")
                            .and(QTlongUser.tlongUser.userType.eq(2)));
                    Iterable<TlongUser> tlongUsers2=appUserRepository.findAll(QTlongUser.tlongUser.orgId.like(tlongUser.getUserName().substring(tlongUser.getUserName().indexOf('-')+1,tlongUser.getUserName().length())+"%")
                            .and(QTlongUser.tlongUser.userType.eq(3)));
                    Iterable<TlongUser> tlongUsers3=appUserRepository.findAll(QTlongUser.tlongUser.orgId.like(tlongUser.getUserName().substring(tlongUser.getUserName().indexOf('-')+1,tlongUser.getUserName().length())+"%")
                            .and(QTlongUser.tlongUser.userType.eq(4)));
                    final int[] count = {0};
                    final int[] count1 = {0};
                    final int[] count2 = {0};
                    for (TlongUser user : tlongUsers) {
                        count[0]++;
                    }
                    for (TlongUser user : tlongUsers2) {
                        count1[0]++;
                    }
                    for (TlongUser user : tlongUsers3) {
                        count2[0]++;
                    }
                    SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                    registerRequsetDto.setId(tlongUser.getId());
                    registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                    registerRequsetDto.setUserName(tlongUser.getUserName());
                    registerRequsetDto.setOrgId(tlongUser.getOrgId());
                    registerRequsetDto.setArea(tlongUser.getArea());
                    registerRequsetDto.setPassword(tlongUser.getPassword());
                    registerRequsetDto.setPhone(tlongUser.getPhone());
                    registerRequsetDto.setWx(tlongUser.getWx());
                    registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
                    registerRequsetDto.setAgentOneNum(count[0]);
                    registerRequsetDto.setAgentTwoNum(count1[0]);
                    registerRequsetDto.setAgentThreeNum(count2[0]);
                    suppliersRegisterRequsetDtos.add(registerRequsetDto);
                }
            }
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUserRole> tlongUserRoles1;
        if (requestDto.getLevel() == 0) {
            tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(11));
        } else if (requestDto.getLevel() == 1) {
            tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(10));
        } else
            tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(7));
        tlongUserRoles1.forEach(tlongUserRole -> {
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                count[0]++;
            } else {
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(tlongUserRole.getUserId())
                        .and(QTlongUser.tlongUser.orgId.eq(requestDto.getOrg())));
                if (tlongUser != null)
                    count[0]++;
            }
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplirtCompany(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUsers = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().eq(2)), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(one -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            registerRequsetDto.setOrgId(one.getOrgId());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().eq(2)));
        tlongUsers1.forEach(tlongUser -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    public PageResponseDto<SuppliersRegisterRequsetDto> findAllManager(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUserRole> tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(6).or(tlongUserRole.roleId.intValue().eq(9)), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUserRoles.forEach(one -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
            TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
            registerRequsetDto.setRoleId(one.getRoleId());
            registerRequsetDto.setId(tlongUser.getId());
            registerRequsetDto.setUserName(tlongUser.getUserName());
            registerRequsetDto.setRealName(tlongUser.getRealName());
            registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        Iterable<TlongUserRole> tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(6).or(tlongUserRole.roleId.intValue().eq(9)));
        final int[] count = {0};
        tlongUserRoles1.forEach(tlongUserRole -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    public PageResponseDto<SuppliersRegisterRequsetDto> findOrgManager(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUserRole> tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(requestDto.getLevel().intValue()), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUserRoles.forEach(one -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
            registerRequsetDto.setRoleId(one.getRoleId());
            TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
            registerRequsetDto.setId(tlongUser.getId());
            registerRequsetDto.setUserName(tlongUser.getUserName());
            registerRequsetDto.setRealName(tlongUser.getRealName());
            registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUserRole> tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(requestDto.getLevel().intValue()));
        tlongUserRoles1.forEach(one -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    public void delManage(Long id, Long roleId) {
        TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.roleId.longValue().eq(roleId).and(tlongUserRole.userId.longValue().eq(id)));
        if (tlongUserRole1 != null)
            tlongUserRoleRepository.delete(tlongUserRole1);
        appUserRepository.delete(id);
    }

    /**
     * 层级搜索代理商分公司
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<SuppliersRegisterRequsetDto> searchAgentByLevel(UserSearchRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        Predicate pre = tlongUser.id.isNotNull();
        final Predicate[] pre1 = {tlongUserRole.id.isNull()};
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        tlongUsers = appUserRepository.findAll(pre, pageRequest);
        tlongUsers.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], tlongUserRole.userId.longValue().eq(one.getId()));
        });
        Page<TlongUserRole> tlongUserRoles;
        if (requestDto.getLevel() == 0) {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.intValue().eq(11));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        } else if (requestDto.getLevel() == 1) {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.intValue().eq(10));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        } else {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.intValue().eq(7));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUserRoles.forEach(one -> {
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                registerRequsetDto.setUserName(tlongUser.getUserName());
                registerRequsetDto.setId(tlongUser.getId());
                registerRequsetDto.setPassword(tlongUser.getPassword());
                registerRequsetDto.setPhone(tlongUser.getPhone());
                registerRequsetDto.setWx(tlongUser.getWx());
                registerRequsetDto.setArea(tlongUser.getArea());
                registerRequsetDto.setOrgId(tlongUser.getOrgId());
                registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
                suppliersRegisterRequsetDtos.add(registerRequsetDto);
            } else {
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(one.getUserId())
                        .and(QTlongUser.tlongUser.orgId.eq(requestDto.getOrg())));
                if (tlongUser != null) {
                    SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                    registerRequsetDto.setId(tlongUser.getId());
                    registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                    registerRequsetDto.setUserName(tlongUser.getUserName());
                    registerRequsetDto.setOrgId(tlongUser.getOrgId());
                    registerRequsetDto.setArea(tlongUser.getArea());
                    registerRequsetDto.setPassword(tlongUser.getPassword());
                    registerRequsetDto.setPhone(tlongUser.getPhone());
                    registerRequsetDto.setWx(tlongUser.getWx());
                    registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
                    suppliersRegisterRequsetDtos.add(registerRequsetDto);
                }
            }
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUserRole> tlongUserRoles1;
        tlongUserRoles1 = tlongUserRoleRepository.findAll(pre1[0]);
        tlongUserRoles1.forEach(tlongUserRole -> {
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                count[0]++;
            } else {
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(tlongUserRole.getUserId())
                        .and(QTlongUser.tlongUser.orgId.eq(requestDto.getOrg())));
                if (tlongUser != null)
                    count[0]++;
            }
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }

    /**
     * 搜索代理商分公司
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplirtCompany(UserSearchRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Predicate pre = tlongUser.id.isNotNull();
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
        pre = ExpressionUtils.and(pre, tlongUser.isCompany.intValue().eq(2));
        Page<TlongUser> tlongUsers = appUserRepository.findAll(pre, pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(one -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            registerRequsetDto.setOrgId(one.getOrgId());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(pre);
        tlongUsers1.forEach(tlongUser -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        return pageSuppliersResponseDto;
    }
}

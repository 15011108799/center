package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.common.user.QTlongUserSettings;
import com.tlong.center.domain.repository.*;
import com.tlong.center.domain.web.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
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
    @Autowired
    private OrderRepository repository;
    JPAQueryFactory queryFactory;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private GoodsRepository repository1;

    @Autowired
    private WebOrgRepository webOrgRepository;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

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
        TlongUser tlongUser1 = appUserRepository.findOne(QTlongUser.tlongUser.userName.eq(requsetDto.getUserName()));
        if (tlongUser1 != null)
            return new Result(0, "用户名已存在");
        /* tlongUser.setPassword(MD5Util.KL(MD5Util.MD5(requsetDto.getPassword())));*/
        WebOrg one = null;
        if (requsetDto.getOrgId() != null)
            one = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requsetDto.getOrgId()));
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
        if (requsetDto.getOrgId() != null && one == null) {
            WebOrg webOrg = new WebOrg();
            webOrg.setOrgName(requsetDto.getOrgId());
            WebOrg webOrg1 = webOrgRepository.save(webOrg);
            tlongUser.setOrgId(webOrg1.getId());
        } else if (one != null)
            tlongUser.setOrgId(one.getId());
        tlongUser.setPhone(requsetDto.getPhone());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
        tlongUser.setGoodsClass(requsetDto.getGoodsClass());
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
        WebOrg one1 = null;
        if (user.getOrgId() != null)
            one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<TlongUser> tlongUsers;
        final Predicate[] pre = {tlongUser.id.isNotNull()};
        if (requestDto.getPtype() != null) {
            if (requestDto.getPtype() != 1 && requestDto.getPtype() != 5 && user.getOrgId() != null) {
                pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.isNotNull());
                pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.intValue().eq(requestDto.getPtype()));
                TlongUserRole tlongUserRoles = tlongUserRoleRepository.findOne(tlongUserRole.roleId.intValue().eq(7).and(tlongUserRole.userId.longValue().eq(user.getId())));
                if (user.getOrgId() != null) {
                    if (tlongUserRoles == null) {
                        WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
                        Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
                        final Predicate[] predicate = {tlongUser.id.isNull()};
                        all.forEach(one2 -> {
                            predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
                        });
                        pre[0] = ExpressionUtils.and(pre[0], predicate[0]);
                    } else {
                        WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(user.getUserName().split("-")[1] + "-" + user.getUserName().split("-")[2] + "-" + user.getUserName().split("-")[3]));
                        pre[0] = ExpressionUtils.and(pre[0], tlongUser.orgId.longValue().eq(one.getId()));
                    }
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.id.longValue().ne(user.getId()));
                }
            } else if (requestDto.getPtype() == 5) {
                if (user.getOrgId() != null) {
                    WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
                    Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
                    final Predicate[] predicate = {tlongUser.id.isNull()};
                    all.forEach(one2 -> {
                        predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
                    });
                    pre[0] = ExpressionUtils.and(pre[0], predicate[0]);
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.id.longValue().ne(user.getId()));
                }
                pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)));
            } else if (requestDto.getPtype() == 1) {
                if (user.getIsCompany() == null)
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.intValue().eq(1));
                else if (user.getIsCompany() == 0)
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.id.longValue().eq(user.getId()));
                else {
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.intValue().eq(1));
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.isCompany.intValue().eq(0));
                    pre[0] = ExpressionUtils.and(pre[0], tlongUser.orgId.longValue().eq(one1.getId()));
                }
            } else {
                pre[0] = ExpressionUtils.and(pre[0], tlongUser.userType.intValue().eq(requestDto.getPtype()));
            }
        }
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.userName.eq(requestDto.getUserName()));
        if (StringUtils.isNotEmpty(requestDto.getUserCode()))
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.userCode.eq(requestDto.getUserCode()));
        if (requestDto.getEsign() == 0 || requestDto.getEsign() == 1)
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.esgin.intValue().eq(requestDto.getEsign()));
        if (requestDto.getAuthentication() == 0 || requestDto.getAuthentication() == 1)
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.authentication.intValue().eq(requestDto.getAuthentication()));
        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
            pre[0] = ExpressionUtils.and(pre[0], tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        tlongUsers = appUserRepository.findAll(pre[0], pageRequest);
        UserSearchResponseDto responseDto = new UserSearchResponseDto();
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(tlongUser1 -> {
            final int[] count1 = {0};
            Iterable<WebGoods> all = repository1.findAll(webGoods.publishUserId.longValue().eq(tlongUser1.getId()).and(webGoods.state.intValue().eq(3)));
            all.forEach(one -> {
                count1[0]++;
            });
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
            WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
            registerRequsetDto.setOrgId(one.getOrgName());
            registerRequsetDto.setPremises(tlongUser1.getPremises());
            registerRequsetDto.setSoldGoodsNum(count1[0]);
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        responseDto.setSuppliersRegisterRequsetDtos(suppliersRegisterRequsetDtos);
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(pre[0]);
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
        else {
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.longValue().eq(one1.getId())), pageRequest);
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            final int[] count1 = {0};
            Iterable<WebGoods> all = repository1.findAll(webGoods.publishUserId.longValue().eq(tlongUser1.getId()).and(webGoods.state.intValue().eq(3)));
            all.forEach(one -> {
                count1[0]++;
            });
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
            WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
            registerRequsetDto.setOrgId(one.getOrgName());
            registerRequsetDto.setPremises(tlongUser1.getPremises());
            registerRequsetDto.setWx(tlongUser1.getWx());
            registerRequsetDto.setUserCode(tlongUser1.getUserCode());
            registerRequsetDto.setSoldGoodsNum(count1[0]);
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
        WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser.getOrgId()));
        registerRequsetDto.setOrgId(one.getOrgName());
        registerRequsetDto.setNickName(tlongUser.getNickName());
        registerRequsetDto.setIsExemption(tlongUser.getIsExemption());
        registerRequsetDto.setUserCode(tlongUser.getUserCode());
        registerRequsetDto.setGoodsClass(tlongUser.getGoodsClass());
        return registerRequsetDto;
    }

    public Result update(SuppliersRegisterRequsetDto requsetDto) {
        WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requsetDto.getOrgId()));
        if (one1==null)
            return new Result(0,"机构不存在");
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
        WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requsetDto.getOrgId()));
        tlongUser.setOrgId(one.getId());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setEsgin(0);
        tlongUser.setGoodsClass(requsetDto.getGoodsClass());
        tlongUser.setAuthentication(0);
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setCompanyName(requsetDto.getCompanyName());
        TlongUser tlongUser1 = appUserRepository.findOne(requsetDto.getId());
        tlongUser.setUserCode(tlongUser1.getUserCode());
        tlongUser.setParentId(tlongUser1.getParentId());
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
        tlongUser.setOrgId(one1.getId());
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
        WebOrg webOrg = null;
        if (user.getOrgId() != null) {
            webOrg = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
        }
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUser2;
        if (user.getUserType() == null) {
            if (requestDto.getPid() == null) {
                if (user.getOrgId() != null) {
                    WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(user.getUserName().split("-")[1] + "-" + user.getUserName().split("-")[2] + "-" + user.getUserName().split("-")[3]));
                    TlongUserRole tlongUserRoles = tlongUserRoleRepository.findOne(tlongUserRole.roleId.intValue().eq(7).and(tlongUserRole.userId.longValue().eq(user.getId())));
                    if (tlongUserRoles == null) {
                        tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%")), pageRequest);
                    } else {
                        Predicate[] predicate = {tlongUser.id.isNotNull()};
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.userType.intValue().eq(requestDto.getType()));
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.orgId.isNotNull());
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.orgId.longValue().eq(webOrg1.getId()));
                        tlongUser2 = appUserRepository.findAll(predicate[0], pageRequest);
                    }
                } else {
                    tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()), pageRequest);
                }
            } else
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.parentId.longValue().eq(requestDto.getPid())), pageRequest);
        } else {
            if (requestDto.getPid() == null) {
                Predicate[] predicates = {tlongUser.id.isNull()};
                WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
                Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
                all.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.userType.intValue().eq(requestDto.getType()));
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.orgId.isNotNull());
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.id.longValue().ne(user.getId()));
                tlongUser2 = appUserRepository.findAll(predicates[0], pageRequest);
            } else {
                tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.pid.longValue().eq(requestDto.getPid()).and(tlongUser.id.longValue().ne(user.getId()))), pageRequest);
            }
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUser2.forEach(tlongUser1 -> {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
            if (user.getOrgId() == null || finalWebOrg.getOrgName().split("-").length == 3 || finalWebOrg.getOrgName().split("-").length - webOrg1.getOrgName().split("-").length == -1) {
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setId(tlongUser1.getId());
                registerRequsetDto.setUserName(tlongUser1.getUserName());
                registerRequsetDto.setUserType(tlongUser1.getUserType());
                registerRequsetDto.setIsCompany(tlongUser1.getIsCompany());
                WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
                registerRequsetDto.setOrgId(one.getOrgName());
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
            if (requestDto.getPid() == null) {
                if (user.getOrgId() != null) {
                    WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(user.getUserName().split("-")[1] + "-" + user.getUserName().split("-")[2] + "-" + user.getUserName().split("-")[3]));
                    TlongUserRole tlongUserRoles = tlongUserRoleRepository.findOne(tlongUserRole.roleId.intValue().eq(7).and(tlongUserRole.userId.longValue().eq(user.getId())));
                    if (tlongUserRoles == null) {
                        tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.orgId.isNotNull()).and(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%")));
                    } else {
                        Predicate[] predicate = {tlongUser.id.isNotNull()};
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.userType.intValue().eq(requestDto.getType()));
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.orgId.isNotNull());
                        predicate[0] = ExpressionUtils.and(predicate[0], tlongUser.orgId.longValue().eq(webOrg1.getId()));
                        tlongUser3 = appUserRepository.findAll(predicate[0]);
                    }
                } else {
                    tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()));
                }
            } else
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.parentId.longValue().eq(requestDto.getPid())));
        } else {
            if (requestDto.getPid() == null) {
                Predicate[] predicates = {tlongUser.id.isNull()};
                WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
                Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
                all.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.userType.intValue().eq(requestDto.getType()));
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.orgId.isNotNull());
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.id.longValue().ne(user.getId()));
                tlongUser3 = appUserRepository.findAll(predicates[0]);
            } else {
                tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(requestDto.getType()).and(tlongUser.parentId.longValue().eq(requestDto.getPid()).and(tlongUser.id.longValue().ne(user.getId()))));
            }
        }
        tlongUser3.forEach(tlongUser1 -> {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
            if (finalWebOrg == null || finalWebOrg.getOrgName() == null || finalWebOrg.getOrgName().split("-").length == 3 || finalWebOrg.getOrgName().split("-").length - webOrg1.getOrgName().split("-").length == -1 || requestDto.getPid() != null) {
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
        WebOrg webOrg = null;
        if (user.getOrgId() != null) {
            webOrg = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
        }
        Iterable<TlongUser> tlongUser3;
        if (type != 5)
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(type).and(tlongUser.esgin.intValue().eq(1).and(tlongUser.authentication.intValue().eq(1))));
        else
            tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)).and(tlongUser.esgin.intValue().eq(1).and(tlongUser.authentication.intValue().eq(1))));
        final int[] count = {0};
        final WebOrg webOrg1 = webOrg;
        tlongUser3.forEach(tlongUser1 -> {
            if (user.getUserType() != null && user.getUserType() != 1) {
                if (user.getOrgId() == null)
                    count[0]++;
                else {
                    WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
                    if (webOrg1.getOrgName().split("-").length == 3 || webOrg1.getOrgName().split("-").length - one.getOrgName().split("-").length == -1) {
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
        WebOrg webOrg = null;
        if (user.getOrgId() != null) {
            webOrg = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
        }
        Iterable<TlongUser> tlongUser3;
        Predicate pre = tlongUser.id.isNotNull();
        if (requestDto.getPtype() != 1 && requestDto.getPtype() != 5 && user.getOrgId() != null) {
            pre = ExpressionUtils.and(pre, tlongUser.userType.isNotNull());
            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getPtype()));
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            pre = ExpressionUtils.and(pre, predicate[0]);
            pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
        } else if (requestDto.getPtype() == 5) {
            if (user.getOrgId() != null) {
                WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
                Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
                final Predicate[] predicate = {tlongUser.id.isNull()};
                all.forEach(one2 -> {
                    predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
                });
                pre = ExpressionUtils.and(pre, predicate[0]);
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
                pre = ExpressionUtils.and(pre, tlongUser.orgId.longValue().eq(webOrg.getId()));
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
        final WebOrg webOrg1 = webOrg;
        tlongUser3.forEach(tlongUser1 -> {
            if (user.getUserType() != null && user.getUserType() != 1) {
                if (user.getOrgId() == null)
                    count[0]++;
                else {
                    WebOrg webOrg2 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
                    if (webOrg1.getOrgName().split("-").length == 3 || webOrg1.getOrgName().split("-").length - webOrg2.getOrgName().split("-").length == -1) {
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
            if (requestDto.getOrg() == null)
                tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(10), pageRequest);
            else {
                Predicate[] predicates = {tlongUserRole.id.isNull()};
                WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                Iterable<TlongUser> all = appUserRepository.findAll(tlongUser.orgId.longValue().eq(one1.getId()));
                all.forEach(one -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUserRole.userId.longValue().eq(one.getId()));
                });
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUserRole.roleId.intValue().eq(10));
                tlongUserRoles = tlongUserRoleRepository.findAll(predicates[0], pageRequest);
            }
        } else {
            if (requestDto.getOrg() == null)
                tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(7), pageRequest);
            else {
                Predicate[] predicates = {tlongUserRole.id.isNull()};
                WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                Iterable<TlongUser> all = appUserRepository.findAll(tlongUser.orgId.longValue().eq(one1.getId()));
                all.forEach(one -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUserRole.userId.longValue().eq(one.getId()));
                });
                predicates[0] = ExpressionUtils.and(predicates[0], tlongUserRole.roleId.intValue().eq(7));
                tlongUserRoles = tlongUserRoleRepository.findAll(predicates[0], pageRequest);
            }
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        tlongUserRoles.forEach(one -> {
            final int[] orderNum = {0};
            final double[] publishPrice = {0.0};
            final double[] founderPrice = {0.0};
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                final Predicate[] pre2 = {webOrder.id.isNull()};
                TlongUser tlongUser1 = appUserRepository.findOne(one.getUserId());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                if (requestDto.getCurrentMonth() != null) {
                    Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                    Predicate[] predicates = {tlongUser.id.isNull()};
                    all1.forEach(two -> {
                        predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                    });
                    Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                    all.forEach(two -> {
                        pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(two.getId()));
                    });
                    if (requestDto.getCurrentMonth() == 1)
                        pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
                    Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                    orders.forEach(order -> {
                        final Predicate[] pre4 = {webOrder.id.isNotNull()};
                        pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                        pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                        pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                        pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                        List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                                webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                                webOrder.state, webOrder.placeOrderTime)
                                .from(tlongUser, webGoods, webOrder)
                                .where(pre4[0])
                                .fetch();
                        tuples.stream().forEach(three -> {
                            if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                                orderNum[0]++;
                                publishPrice[0] += three.get(webGoods.publishPrice);
                                founderPrice[0] += three.get(webGoods.founderPrice);
                                orderNumTotal[0]++;
                                publishPriceTotal[0] += three.get(webGoods.publishPrice);
                                founderPriceTotal[0] += three.get(webGoods.founderPrice);
                            }
                        });
                    });
                }
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                registerRequsetDto.setUserName(tlongUser1.getUserName());
                registerRequsetDto.setId(tlongUser1.getId());
                registerRequsetDto.setPassword(tlongUser1.getPassword());
                registerRequsetDto.setPhone(tlongUser1.getPhone());
                registerRequsetDto.setWx(tlongUser1.getWx());
                registerRequsetDto.setArea(tlongUser1.getArea());
                registerRequsetDto.setOrgId(null);
                registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
                registerRequsetDto.setOrderNum(orderNum[0]);
                registerRequsetDto.setFounderPrice(founderPrice[0]);
                suppliersRegisterRequsetDtos.add(registerRequsetDto);
            } else {
                final Predicate[] pre2 = {webOrder.id.isNull()};
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                WebOrg one2 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                TlongUser tlongUser1 = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(one.getUserId())
                        .and(QTlongUser.tlongUser.orgId.longValue().eq(one2.getId())));
                if (tlongUser1 != null) {
                    if (requestDto.getCurrentMonth() != null) {
                        Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                        Predicate[] predicates = {tlongUser.id.isNull()};
                        all1.forEach(two -> {
                            predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                        });
                        Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                        all.forEach(two -> {
                            pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(two.getId()));
                        });
                        if (requestDto.getCurrentMonth() != null && requestDto.getCurrentMonth() == 1)
                            pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
                        Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                        orders.forEach(order -> {
                            final Predicate[] pre4 = {webOrder.id.isNotNull()};
                            pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                            pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));

                            List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                                    webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                                    webOrder.state, webOrder.placeOrderTime)
                                    .from(tlongUser, webGoods, webOrder)
                                    .where(pre4[0])
                                    .fetch();

                            tuples.stream().forEach(three -> {
                                if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                                    orderNum[0]++;
                                    publishPrice[0] += three.get(webGoods.publishPrice);
                                    founderPrice[0] += three.get(webGoods.founderPrice);
                                    orderNumTotal[0]++;
                                    publishPriceTotal[0] += three.get(webGoods.publishPrice);
                                    founderPriceTotal[0] += three.get(webGoods.founderPrice);
                                }
                            });
                        });
                    }
                }
                if (tlongUser1 != null) {
                    Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                    Predicate[] predicates = {tlongUser.id.isNull()};
                    Predicate[] predicates1 = {tlongUser.id.isNull()};
                    Predicate[] predicates2 = {tlongUser.id.isNull()};
                    all1.forEach(two -> {
                        predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                        predicates1[0] = ExpressionUtils.or(predicates1[0], tlongUser.orgId.longValue().eq(two.getId()));
                        predicates2[0] = ExpressionUtils.or(predicates2[0], tlongUser.orgId.longValue().eq(two.getId()));
                    });
                    predicates[0] = ExpressionUtils.and(predicates[0], QTlongUser.tlongUser.userType.eq(2));
                    predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.esgin.intValue().eq(1));
                    predicates[0] = ExpressionUtils.and(predicates[0], tlongUser.authentication.intValue().eq(1));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], QTlongUser.tlongUser.userType.eq(3));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], tlongUser.esgin.intValue().eq(1));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], tlongUser.authentication.intValue().eq(1));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], QTlongUser.tlongUser.userType.eq(4));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], tlongUser.esgin.intValue().eq(1));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], tlongUser.authentication.intValue().eq(1));
                    Iterable<TlongUser> tlongUsers = appUserRepository.findAll(predicates[0]);
                    Iterable<TlongUser> tlongUsers2 = appUserRepository.findAll(predicates1[0]);
                    Iterable<TlongUser> tlongUsers3 = appUserRepository.findAll(predicates2[0]);
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
                    registerRequsetDto.setId(tlongUser1.getId());
                    registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                    registerRequsetDto.setUserName(tlongUser1.getUserName());
                    WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
                    registerRequsetDto.setOrgId(one1.getOrgName());
                    registerRequsetDto.setArea(tlongUser1.getArea());
                    registerRequsetDto.setPassword(tlongUser1.getPassword());
                    registerRequsetDto.setPhone(tlongUser1.getPhone());
                    registerRequsetDto.setWx(tlongUser1.getWx());
                    registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
                    registerRequsetDto.setAgentOneNum(count[0]);
                    registerRequsetDto.setAgentTwoNum(count1[0]);
                    registerRequsetDto.setAgentThreeNum(count2[0]);
                    registerRequsetDto.setOrderNum(orderNum[0]);
                    registerRequsetDto.setFounderPrice(founderPrice[0]);
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
                WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(tlongUserRole.getUserId())
                        .and(QTlongUser.tlongUser.orgId.longValue().eq(one1.getId())));
                if (tlongUser != null)
                    count[0]++;
            }
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setFounderPrice(founderPriceTotal[0]);
        return pageSuppliersResponseDto;
    }

    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplirtCompany(PageAndSortRequestDto requestDto) {
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUser> tlongUsers = appUserRepository.findAll(tlongUser.isCompany.intValue().eq(2), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        tlongUsers.forEach(one -> {
            final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
            final Predicate[] pre2 = {webOrder.id.isNull()};
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.isCompany.intValue().ne(2).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(one.getOrgId())));
            tlongUser3.forEach(two -> {
                pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(two.getId()));
            });
            Iterable<WebGoods> appGoods1 = repository1.findAll(pre[0]);
            List<Long> ids = new ArrayList<>();
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
            ids.forEach(three -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.goodsId.longValue().eq(three));
            });
            final int[] orderNum = {0};
            final double[] publishPrice = {0.0};
            final double[] founderPrice = {0.0};
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (requestDto.getCurrentMonth() != null) {
                if (requestDto.getCurrentMonth() == 1)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
                Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                orders.forEach(order -> {
                    final Predicate[] pre4 = {webOrder.id.isNotNull()};
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                    List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                            webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                            webOrder.state, webOrder.placeOrderTime)
                            .from(tlongUser, webGoods, webOrder)
                            .where(pre4[0])
                            .fetch();
                    tuples.stream().forEach(three -> {
                        if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                            orderNum[0]++;
                            publishPrice[0] += three.get(webGoods.publishPrice);
                            founderPrice[0] += three.get(webGoods.founderPrice);
                            orderNumTotal[0]++;
                            publishPriceTotal[0] += three.get(webGoods.publishPrice);
                            founderPriceTotal[0] += three.get(webGoods.founderPrice);
                        }
                    });
                });
            }
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
            registerRequsetDto.setOrgId(one1.getOrgName());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            registerRequsetDto.setRoleName("供应商分公司");
            registerRequsetDto.setOrderNum(orderNum[0]);
            registerRequsetDto.setPublishPrice(publishPrice[0]);
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().eq(2)));
        tlongUsers1.forEach(tlongUser -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setPublishPrice(publishPriceTotal[0]);
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
        Iterable<TlongUser> tlongUsers;
        Predicate pre = tlongUser.id.isNotNull();
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        final Predicate[] pre1 = {tlongUserRole.id.isNull()};
        if (StringUtils.isNotEmpty(requestDto.getUserName())) {
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        }
        if (requestDto.getCurrentMonth() == null) {
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        }
        tlongUsers = appUserRepository.findAll(pre);
        tlongUsers.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], tlongUserRole.userId.longValue().eq(one.getId()));
        });
        Page<TlongUserRole> tlongUserRoles;
        if (requestDto.getLevel() == 0) {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.longValue().eq((long) 11));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        } else if (requestDto.getLevel() == 1) {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.longValue().eq((long) 10));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        } else {
            pre1[0] = ExpressionUtils.and(pre1[0], tlongUserRole.roleId.longValue().eq((long) 7));
            tlongUserRoles = tlongUserRoleRepository.findAll(pre1[0], pageRequest);
        }
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUserRoles.forEach(one -> {
            if (StringUtils.isEmpty(requestDto.getOrg())) {
                final Predicate[] pre2 = {webOrder.id.isNull()};
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
                TlongUser tlongUser1 = appUserRepository.findOne(one.getUserId());
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                all.forEach(two -> {
                    pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(two.getId()));
                });
                if (requestDto.getStartTime() != null && requestDto.getEndTime() != null) {
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
                } else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
                else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
                Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                final int[] orderNum = {0};
                final double[] publishPrice = {0.0};
                final double[] founderPrice = {0.0};
                orders.forEach(order -> {
                    final Predicate[] pre4 = {webOrder.id.isNotNull()};
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                    List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                            webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                            webOrder.state, webOrder.placeOrderTime)
                            .from(tlongUser, webGoods, webOrder)
                            .where(pre4[0])
                            .fetch();

                    tuples.stream().forEach(three -> {
                        if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                            orderNum[0]++;
                            publishPrice[0] += three.get(webGoods.publishPrice);
                            founderPrice[0] += three.get(webGoods.founderPrice);
                            orderNumTotal[0]++;
                            publishPriceTotal[0] += three.get(webGoods.publishPrice);
                            founderPriceTotal[0] += three.get(webGoods.founderPrice);
                        }
                    });
                });
                TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                registerRequsetDto.setUserName(tlongUser.getUserName());
                registerRequsetDto.setId(tlongUser.getId());
                registerRequsetDto.setPassword(tlongUser.getPassword());
                registerRequsetDto.setPhone(tlongUser.getPhone());
                registerRequsetDto.setWx(tlongUser.getWx());
                registerRequsetDto.setArea(tlongUser.getArea());
                WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser.getOrgId()));
                registerRequsetDto.setOrgId(one1.getOrgName());
                registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
                registerRequsetDto.setOrderNum(orderNum[0]);
                registerRequsetDto.setFounderPrice(founderPrice[0]);
                suppliersRegisterRequsetDtos.add(registerRequsetDto);
            } else {
                final int[] orderNum = {0};
                final double[] publishPrice = {0.0};
                final double[] founderPrice = {0.0};
                final Predicate[] pre2 = {webOrder.id.isNull()};
                WebOrg one2 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                TlongUser tlongUser1 = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(one.getUserId())
                        .and(QTlongUser.tlongUser.orgId.longValue().eq(one2.getId())));
                if (tlongUser1 != null) {
                    Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                    Predicate[] predicates = {tlongUser.id.isNull()};
                    all1.forEach(two -> {
                        predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                    });
                    Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                    all.forEach(two -> {
                        pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(two.getId()));
                    });
                    if (requestDto.getStartTime() != null && requestDto.getEndTime() != null) {
                        pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
                    } else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                        pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
                    else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                        pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
                    Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                    orders.forEach(order -> {
                        final Predicate[] pre4 = {webOrder.id.isNotNull()};
                        pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                        pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                        pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                        pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                        List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                                webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                                webOrder.state, webOrder.placeOrderTime)
                                .from(tlongUser, webGoods, webOrder)
                                .where(pre4[0])
                                .fetch();

                        tuples.stream().forEach(three -> {
                            if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                                orderNum[0]++;
                                publishPrice[0] += three.get(webGoods.publishPrice);
                                founderPrice[0] += three.get(webGoods.founderPrice);
                                orderNumTotal[0]++;
                                publishPriceTotal[0] += three.get(webGoods.publishPrice);
                                founderPriceTotal[0] += three.get(webGoods.founderPrice);
                            }
                        });
                    });
                    Iterable<WebOrg> all2 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(tlongUser1.getUserName().substring(tlongUser1.getUserName().indexOf('-') + 1, tlongUser1.getUserName().length()) + "%"));
                    Predicate[] predicates0 = {tlongUser.id.isNull()};
                    Predicate[] predicates1 = {tlongUser.id.isNull()};
                    Predicate[] predicates2 = {tlongUser.id.isNull()};
                    all2.forEach(two -> {
                        predicates0[0] = ExpressionUtils.or(predicates0[0], tlongUser.orgId.longValue().eq(two.getId()));
                        predicates1[0] = ExpressionUtils.or(predicates1[0], tlongUser.orgId.longValue().eq(two.getId()));
                        predicates2[0] = ExpressionUtils.or(predicates2[0], tlongUser.orgId.longValue().eq(two.getId()));
                    });
                    predicates0[0] = ExpressionUtils.and(predicates0[0], QTlongUser.tlongUser.userType.eq(2));
                    predicates0[0] = ExpressionUtils.and(predicates0[0], tlongUser.esgin.intValue().eq(1));
                    predicates0[0] = ExpressionUtils.and(predicates0[0], tlongUser.authentication.intValue().eq(1));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], QTlongUser.tlongUser.userType.eq(3));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], tlongUser.esgin.intValue().eq(1));
                    predicates1[0] = ExpressionUtils.and(predicates1[0], tlongUser.authentication.intValue().eq(1));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], QTlongUser.tlongUser.userType.eq(4));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], tlongUser.esgin.intValue().eq(1));
                    predicates2[0] = ExpressionUtils.and(predicates2[0], tlongUser.authentication.intValue().eq(1));
                    Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(predicates0[0]);
                    Iterable<TlongUser> tlongUsers2 = appUserRepository.findAll(predicates1[0]);
                    Iterable<TlongUser> tlongUsers3 = appUserRepository.findAll(predicates2[0]);
                    final int[] count = {0};
                    final int[] count1 = {0};
                    final int[] count2 = {0};
                    for (TlongUser user : tlongUsers1) {
                        count[0]++;
                    }
                    for (TlongUser user : tlongUsers2) {
                        count1[0]++;
                    }
                    for (TlongUser user : tlongUsers3) {
                        count2[0]++;
                    }
                    SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                    registerRequsetDto.setId(tlongUser1.getId());
                    registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
                    registerRequsetDto.setUserName(tlongUser1.getUserName());
                    WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(tlongUser1.getOrgId()));
                    registerRequsetDto.setOrgId(one1.getOrgName());
                    registerRequsetDto.setArea(tlongUser1.getArea());
                    registerRequsetDto.setPassword(tlongUser1.getPassword());
                    registerRequsetDto.setPhone(tlongUser1.getPhone());
                    registerRequsetDto.setWx(tlongUser1.getWx());
                    registerRequsetDto.setAgentOneNum(count[0]);
                    registerRequsetDto.setAgentTwoNum(count1[0]);
                    registerRequsetDto.setAgentThreeNum(count2[0]);
                    registerRequsetDto.setOrderNum(orderNum[0]);
                    registerRequsetDto.setFounderPrice(founderPrice[0]);
                    registerRequsetDto.setRegistDate(tlongUser1.getRegistDate());
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
                WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
                TlongUser tlongUser = appUserRepository.findOne(QTlongUser.tlongUser.id.longValue().eq(tlongUserRole.getUserId())
                        .and(QTlongUser.tlongUser.orgId.longValue().eq(one1.getId())));
                if (tlongUser != null)
                    count[0]++;
            }
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setFounderPrice(founderPriceTotal[0]);
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
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
        if (requestDto.getCurrentMonth() == null) {
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
        }
        pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
        pre = ExpressionUtils.and(pre, tlongUser.isCompany.intValue().eq(2));
        Page<TlongUser> tlongUsers = appUserRepository.findAll(pre, pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        tlongUsers.forEach(one -> {
            final Predicate[] pre1 = {QWebGoods.webGoods.id.isNull()};
            final Predicate[] pre2 = {webOrder.id.isNull()};
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.isCompany.intValue().ne(2).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.longValue().eq(one.getOrgId())));
            tlongUser3.forEach(two -> {
                pre1[0] = ExpressionUtils.or(pre1[0], QWebGoods.webGoods.publishUserId.longValue().eq(two.getId()));
            });
            Iterable<WebGoods> appGoods1 = repository1.findAll(pre1[0]);
            List<Long> ids = new ArrayList<>();
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
            ids.forEach(three -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.goodsId.longValue().eq(three));
            });
            final int[] orderNum = {0};
            final double[] publishPrice = {0.0};
            final double[] founderPrice = {0.0};
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (requestDto.getCurrentMonth() != null) {
                if (requestDto.getStartTime() != null && requestDto.getEndTime() != null) {
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
                } else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
                else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
                Iterable<WebOrder> orders = repository.findAll(pre2[0]);
                orders.forEach(order -> {
                    final Predicate[] pre4 = {webOrder.id.isNotNull()};
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                    if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                        pre4[0] = ExpressionUtils.and(pre4[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
                    else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                        pre4[0] = ExpressionUtils.and(pre4[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
                    else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                        pre4[0] = ExpressionUtils.and(pre4[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
                    List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                            webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                            webOrder.state, webOrder.placeOrderTime)
                            .from(tlongUser, webGoods, webOrder)
                            .where(pre4[0])
                            .fetch();
                    tuples.stream().forEach(three -> {
                        if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                            orderNum[0]++;
                            publishPrice[0] += three.get(webGoods.publishPrice);
                            founderPrice[0] += three.get(webGoods.founderPrice);
                            orderNumTotal[0]++;
                            publishPriceTotal[0] += three.get(webGoods.publishPrice);
                            founderPriceTotal[0] += three.get(webGoods.founderPrice);
                        }
                    });
                });
            }
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
            registerRequsetDto.setOrgId(one1.getOrgName());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            registerRequsetDto.setRoleName("供应商分公司");
            registerRequsetDto.setOrderNum(orderNum[0]);
            registerRequsetDto.setPublishPrice(publishPrice[0]);
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(pre);
        tlongUsers1.forEach(tlongUser -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setPublishPrice(publishPriceTotal[0]);
        return pageSuppliersResponseDto;
    }

    /**
     * 查询某公司所有供应商
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<SuppliersRegisterRequsetDto> findSupplierByOrg(PageAndSortRequestDto requestDto) {
        Page<TlongUser> tlongUser2;
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        WebOrg one2 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
        tlongUser2 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.longValue().eq(one2.getId())), pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        tlongUser2.forEach(one -> {
            final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
            final Predicate[] pre2 = {webOrder.id.isNull()};
            pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
            Iterable<WebGoods> appGoods1 = repository1.findAll(pre[0]);
            List<Long> ids = new ArrayList<>();
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
            ids.forEach(three -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.goodsId.longValue().eq(three));
            });
            final int[] orderNum = {0};
            final double[] publishPrice = {0.0};
            final double[] founderPrice = {0.0};
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            if (requestDto.getCurrentMonth() == 1)
                pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
            Iterable<WebOrder> orders = repository.findAll(pre2[0]);
            orders.forEach(order -> {
                final Predicate[] pre4 = {webOrder.id.isNotNull()};
                pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                        webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                        webOrder.state, webOrder.placeOrderTime)
                        .from(tlongUser, webGoods, webOrder)
                        .where(pre4[0])
                        .fetch();
                tuples.stream().forEach(three -> {
                    if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                        orderNum[0]++;
                        publishPrice[0] += three.get(webGoods.publishPrice);
                        founderPrice[0] += three.get(webGoods.founderPrice);
                        orderNumTotal[0]++;
                        publishPriceTotal[0] += three.get(webGoods.publishPrice);
                        founderPriceTotal[0] += three.get(webGoods.founderPrice);
                    }
                });
            });
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
            registerRequsetDto.setOrgId(one1.getOrgName());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            registerRequsetDto.setOrderNum(orderNum[0]);
            registerRequsetDto.setPublishPrice(publishPrice[0]);
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3;
        WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
        tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.longValue().eq(one1.getId())));
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setPublishPrice(publishPriceTotal[0]);
        return pageSuppliersResponseDto;
    }

    public PageResponseDto<SuppliersRegisterRequsetDto> searchSupplierByOrg(UserSearchRequestDto requestDto) {
        Page<TlongUser> tlongUser2;
        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Predicate preUser = tlongUser.id.isNotNull();
        if (StringUtils.isNotEmpty(requestDto.getUserName()))
            preUser = ExpressionUtils.and(preUser, tlongUser.userName.eq(requestDto.getUserName()));
        preUser = ExpressionUtils.and(preUser, tlongUser.userType.intValue().eq(1));
        preUser = ExpressionUtils.and(preUser, tlongUser.isCompany.intValue().ne(2));
        WebOrg one2 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
        preUser = ExpressionUtils.and(preUser, tlongUser.orgId.longValue().eq(one2.getId()));
        tlongUser2 = appUserRepository.findAll(preUser, pageRequest);
        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        final int[] orderNumTotal = {0};
        final double[] publishPriceTotal = {0.0};
        final double[] founderPriceTotal = {0.0};
        tlongUser2.forEach(one -> {
            final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
            final Predicate[] pre2 = {webOrder.id.isNull()};
            pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
            Iterable<WebGoods> appGoods1 = repository1.findAll(pre[0]);
            List<Long> ids = new ArrayList<>();
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
            ids.forEach(three -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.goodsId.longValue().eq(three));
            });
            final int[] orderNum = {0};
            final double[] publishPrice = {0.0};
            final double[] founderPrice = {0.0};
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null) {
                pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            } else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
            Iterable<WebOrder> orders = repository.findAll(pre2[0]);
            orders.forEach(order -> {
                final Predicate[] pre4 = {webOrder.id.isNotNull()};
                pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
                pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
                pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
                pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
                List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
                        webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                        webOrder.state, webOrder.placeOrderTime)
                        .from(tlongUser, webGoods, webOrder)
                        .where(pre4[0])
                        .fetch();
                tuples.stream().forEach(three -> {
                    if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
                        orderNum[0]++;
                        publishPrice[0] += three.get(webGoods.publishPrice);
                        founderPrice[0] += three.get(webGoods.founderPrice);
                        orderNumTotal[0]++;
                        publishPriceTotal[0] += three.get(webGoods.publishPrice);
                        founderPriceTotal[0] += three.get(webGoods.founderPrice);
                    }
                });
            });
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setPhone(one.getPhone());
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
            registerRequsetDto.setOrgId(one1.getOrgName());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            registerRequsetDto.setOrderNum(orderNum[0]);
            registerRequsetDto.setPublishPrice(publishPrice[0]);
            suppliersRegisterRequsetDtos.add(registerRequsetDto);
        });
        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
        final int[] count = {0};
        Iterable<TlongUser> tlongUser3;
        WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requestDto.getOrg()));
        tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.longValue().eq(one1.getId())));
        tlongUser3.forEach(tlongUser1 -> {
            count[0]++;
        });
        pageSuppliersResponseDto.setCount(count[0]);
        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
        pageSuppliersResponseDto.setPublishPrice(publishPriceTotal[0]);
        return pageSuppliersResponseDto;
    }
}

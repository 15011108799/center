package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.Result;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.user.*;
import com.tlong.center.api.dto.web.FindUserPublishNumResponseDto;
import com.tlong.center.api.dto.web.UpdateUserPublishNumRequsetDto;
import com.tlong.center.api.dto.web.org.AddOrgRequestDto;
import com.tlong.center.api.dto.web.org.SuppliersCompanyRequestDto;
import com.tlong.center.api.dto.web.org.SuppliersCompanyResponseDto;
import com.tlong.center.api.dto.web.user.AddManagerRequestDto;
import com.tlong.center.api.dto.web.user.TlongUserResponseDto;
import com.tlong.center.api.exception.CustomException;
import com.tlong.center.common.user.UserSettingsSerivce;
import com.tlong.center.common.utils.MD5Util;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.common.user.QTlongUserSettings;
import com.tlong.center.domain.common.user.TlongUserSettings;
import com.tlong.center.domain.repository.*;
import com.tlong.center.domain.web.*;
import com.tlong.core.utils.PropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
import static com.tlong.center.domain.web.QTlongUserRole.tlongUserRole;
import static com.tlong.center.domain.web.QWebOrder.webOrder;

@Component
@Transactional
public class UserService {
    private final AppUserRepository appUserRepository;
    private final TlongUserRoleRepository tlongUserRoleRepository;
    private final TlongUserSettingsRepository settingsRepository;
    private final TlongRoleRepository tlongRoleRepository;
    private final CodeService codeService;
    private final OrderRepository repository;
    private final EntityManager entityManager;
    private final GoodsRepository repository1;
    private final WebOrgRepository webOrgRepository;
    private final TlongUserSettingsRepository tlongUserSettingsRepository;
    private final UserSettingsSerivce settingsService;
    private final WebOrgService webOrgService;

    private JPAQueryFactory queryFactory;

    private final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    public UserService(AppUserRepository appUserRepository, TlongUserRoleRepository tlongUserRoleRepository, TlongUserSettingsRepository settingsRepository, TlongRoleRepository tlongRoleRepository, CodeService codeService, OrderRepository repository, EntityManager entityManager, GoodsRepository repository1, WebOrgRepository webOrgRepository, TlongUserSettingsRepository tlongUserSettingsRepository, UserSettingsSerivce settingsService, WebOrgService webOrgService) {
        this.appUserRepository = appUserRepository;
        this.tlongUserRoleRepository = tlongUserRoleRepository;
        this.settingsRepository = settingsRepository;
        this.tlongRoleRepository = tlongRoleRepository;
        this.codeService = codeService;
        this.repository = repository;
        this.entityManager = entityManager;
        this.repository1 = repository1;
        this.webOrgRepository = webOrgRepository;
        this.tlongUserSettingsRepository = tlongUserSettingsRepository;
        this.settingsService = settingsService;
        this.webOrgService = webOrgService;
    }

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    /**
     * 创建管理员用户
     */
    public TlongResultDto createManager(AddManagerRequestDto requestDto) {
        this.userNameCheck(requestDto.getUserName());
        //用户表中创建用户基本信息
        TlongUser tlongUser = new TlongUser();
        tlongUser.setUserName(requestDto.getUserName());
        tlongUser.setPhone(requestDto.getPhone());
        tlongUser.setWx(requestDto.getWx());
        tlongUser.setIsCompany(1);
        tlongUser.setOrgId(requestDto.getOrgId());
        tlongUser.setRealName(requestDto.getRealName());

        //生成用户编码 管理员用户
        tlongUser.setUserCode(codeService.createAllCode(0, null, 1,1));
        //设置机构信息
        tlongUser.setOrgId(requestDto.getOrgId());
        tlongUser.setCurState(1);
        tlongUser.setIsDeleted(0);

        //设置用户角色表


        tlongUser.setPassword(MD5Util.MD5(requestDto.getPassword()));
        TlongUser newUser = appUserRepository.save(tlongUser);
        logger.info("管理员创建成功!" + newUser.getId());
        //用户角色表进行关联
        TlongUserRole tlongUserRole = new TlongUserRole();
        tlongUserRole.setUserId(newUser.getId());
        tlongUserRole.setRoleId(requestDto.getRoleId());
        TlongUserRole newUserRole = tlongUserRoleRepository.save(tlongUserRole);
        logger.info("管理员关系表设置完成!" + newUserRole.getId());

        return new TlongResultDto(0, "管理员注册成功!");
    }


    /**
     * 用户注册用户名判重
     */
    public TlongResultDto userNameCheck(String userName){
        //TODO 用户名判空放到前台做  密码长度判断放到前台做
        //用户名判重
        Iterable<TlongUser> tlongUser1 = appUserRepository.findAll(QTlongUser.tlongUser.userName.eq(userName));
        if (tlongUser1 != null){
            return new TlongResultDto(0, "用户名已存在");
        }else {
            return new TlongResultDto(1,"用户名可用");
        }

    }

    /**
     * 用户注册
     */
    public Result suppliersRegister(SuppliersRegisterRequsetDto requsetDto) {
        TlongUser tlongUser = new TlongUser();
        //用户名判重
        TlongResultDto tlongResultDto = this.userNameCheck(requsetDto.getUserName());
        if (tlongResultDto.getResult() == 0){
            return new Result(0, "用户名已存在");
        }
        //加密密码
        tlongUser.setPassword(MD5Util.MD5(requsetDto.getPassword()));

        //生成用户编码 TODO 需要看怎么优化
        tlongUser.setUserCode(codeService.createAllCode(0, requsetDto.getUserType(), 1,requsetDto.getIsCompany()));
//        if (requsetDto.getUserType() != null && requsetDto.getUserType() == 1) {
//            if (requsetDto.getIsCompany() == 0)
//                tlongUser.setUserCode(codeService.createAllCode(0, 0, 1,requsetDto.getIsCompany()));
//            else
//                tlongUser.setUserCode(codeService.createAllCode(0, 1, 1,requsetDto.getIsCompany()));
//        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 2) {
//            tlongUser.setUserCode(codeService.createAllCode(0, 0, 1,requsetDto.getIsCompany()));
//        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 3) {
//            tlongUser.setUserCode(codeService.createAllCode(0, 1, 1,requsetDto.getIsCompany()));
//        } else if (requsetDto.getUserType() != null && requsetDto.getUserType() == 4) {
//            tlongUser.setUserCode(codeService.createAllCode(0, 2, 1,requsetDto.getIsCompany()));
//        }

        //设置机构id 如果机构id不为空
        if (requsetDto.getOrgId() != null) {
            WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.id.eq(requsetDto.getOrgId()));
            if (Objects.nonNull(one)){
                tlongUser.setOrgId(one.getId());
            }else {
                throw new RuntimeException("传递的参数中机构id不存在");
                //创建对应机构
//                AddOrgRequestDto addOrgRequestDto = new AddOrgRequestDto();
//                addOrgRequestDto.setOrgName(requsetDto.getOrgId());
//                TlongResultDto tlongResultDto1 = webOrgService.addOrg(addOrgRequestDto);
//                tlongUser.setOrgId(Long.valueOf(tlongResultDto1.getContent()));
//                tlongUser.setParentId(1426L);
            }
        }


        tlongUser.setLevel(requsetDto.getUserType());
        tlongUser.setUserName(requsetDto.getUserName());
        tlongUser.setUserType(requsetDto.getUserType());
        tlongUser.setIsCompany(requsetDto.getIsCompany());
        tlongUser.setRealName(requsetDto.getRealName());
        tlongUser.setBirthday(requsetDto.getBirthday());
        tlongUser.setSex(requsetDto.getSex());
        tlongUser.setArea(requsetDto.getArea());
        tlongUser.setWx(requsetDto.getWx());
        tlongUser.setServiceHotline(requsetDto.getServiceHotline());
        tlongUser.setHeadImage(requsetDto.getHeadImage());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        tlongUser.setRegistDate(simpleDateFormat.format(new Date()));
        tlongUser.setPremises(requsetDto.getPremises());
        tlongUser.setPhone(requsetDto.getPhone());
        tlongUser.setNickName(requsetDto.getNickName());
        tlongUser.setIsExemption(requsetDto.getIsExemption());
        tlongUser.setEsgin(0);
        tlongUser.setAuthentication(0);
        tlongUser.setGoodsClass(requsetDto.getGoodsClass());
        //设置用户发布数量
        if (requsetDto.getUserType() == 0){
            //默认设置的用户类型总是比用户类型大2
            TlongUserSettings one = settingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.eq(0)
                    .and(QTlongUserSettings.tlongUserSettings.userId.isNull()));
            if (Objects.nonNull(one)){
                tlongUser.setGoodsPublishNum(one.getGoodsReleaseNumber());
            }
        }
        TlongUser user = appUserRepository.save(tlongUser);

        //判断注册的人是不是管理员 设置用户角色关系表
        if (requsetDto.getUserType() == null){
            //设置关系表
            TlongUserRole tlongUserRole = new TlongUserRole();
            tlongUserRole.setUserId(user.getId());
            tlongUserRole.setRoleId(requsetDto.getRoleId());
            tlongUserRoleRepository.save(tlongUserRole);
        }

        //给该用户设置基本发布商品的参数
        if (requsetDto.getUserType() != null){
            settingsService.addUserSettings(user.getId(),requsetDto.getUserType(),requsetDto.getIsCompany());

        }

        if (user == null) {
            return new Result(0, "注册失败");
        }
        return new Result(1, "注册成功");
    }


    /**
     * 删除用户
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
     */
    public Page<SuppliersRegisterRequsetDto> searchUser(UserSearchRequestDto requestDto, MultiValueMap<String,String> params) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        //模糊搜索
        Predicate[] resove = resove(params);
        Predicate[] pre = {tlongUser.id.isNotNull()};
//        resove.forEach(one -> pre[0] = ExpressionUtils.and(pre[0], one));
        //判断用户是不是管理员
        TlongUser one1 = appUserRepository.findOne(requestDto.getUserId());
        Page<TlongUser> all = null;
        if (Objects.isNull(one1)) {
            throw new RuntimeException("当前用户不存在");
        }
        if (one1.getUserType() == null) {
            //超级管理员 查出所有的用户
            //获取自己的机构id
            WebOrg one = webOrgRepository.findOne(one1.getOrgId());
            Long orgId =one.getId();
            //获取所有的子级机构id集合
            Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.parentOrgId.eq(orgId)
                .or(QWebOrg.webOrg.id.eq(orgId)));
            List<WebOrg> tlongUsers = ToListUtil.IterableToList(all1);
            List<Long> orgIds = tlongUsers.stream().map(WebOrg::getId).collect(Collectors.toList());
            all = appUserRepository.findAll(tlongUser.userType.isNotNull()
                    .and(tlongUser.orgId.in(orgIds))
//                    .and(tlongUser.id.ne(requestDto.getUserId()))
                    .and(pre[0]), pageRequest);
        } else if (one1.getUserType() == 1) {
            //总代
            all = appUserRepository.findAll(tlongUser.parentId.eq(requestDto.getUserId())
                    .and(tlongUser.userType.isNotNull())
//                    .and(tlongUser.id.ne(requestDto.getUserId()))
                    .and(pre[0]), pageRequest);
        } else if (one1.getUserType() == 2) {
            all = appUserRepository.findAll(tlongUser.id.eq(requestDto.getUserId())
                    .and(tlongUser.userType.isNotNull())
                    .and(pre[0]), pageRequest);
        }

        if (all != null) {
            Page<SuppliersRegisterRequsetDto> map = all.map(one -> {
                SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
                registerRequsetDto.setId(one.getId());
                registerRequsetDto.setRegistDate(one.getRegistDate());
                registerRequsetDto.setUserName(one.getUserName());
                registerRequsetDto.setUserType(one.getUserType());
                registerRequsetDto.setIsCompany(one.getIsCompany());
                registerRequsetDto.setRealName(one.getRealName());
                registerRequsetDto.setBirthday(one.getBirthday());
                registerRequsetDto.setSex(one.getSex());
                registerRequsetDto.setWx(one.getWx());
                registerRequsetDto.setUserCode(one.getUserCode());
                registerRequsetDto.setNickName(one.getNickName());
                registerRequsetDto.setEsgin(one.getEsgin());
                registerRequsetDto.setAuthentication(one.getAuthentication());
                WebOrg one2 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
                registerRequsetDto.setOrgId(one2.getId());
                //orgName
                registerRequsetDto.setPremises(one.getPremises());
                return registerRequsetDto;
            });
            return map;
        }
        return null;
    }


    /**
     * 用户是否认证
     */
    public Boolean authentication(Long id) {
        TlongUser tlongUser = appUserRepository.findOne(id);
        return tlongUser != null && tlongUser.getEsgin() != null && 1 == tlongUser.getEsgin();
    }


    /**
     * 本地方法   根据用户列表获取用户机构名称
     */
    private Map<Long,String> orgNameMap(Page<TlongUser> tlongUser2){
        Map<Long,String> orgNameMap = new HashMap<>();
        List<Long> orgIds = new ArrayList<>();
        tlongUser2.forEach(one -> orgIds.add(one.getOrgId()));
        Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.id.in(orgIds));
        all.forEach(one -> orgNameMap.put(one.getId(),one.getOrgName()));
        return orgNameMap;
    }

    /**
     * 本地方法 根据用户获得用户下面所有的代理商或者供应商用户id集合带分页的
     */
    private Page<TlongUser> childrenUserIds(TlongUser user,PageRequest pageRequest){
        Iterable<WebOrg> all;
        if(user.getUserType() == null){
            //管理员用户获取当前集团下的所有机构的机构集合
            TlongUser tlongUser1 = appUserRepository.findOne(user.getId());
            //判断省市区三级别
            WebOrg one = webOrgRepository.findOne(tlongUser1.getOrgId());
            if (one.getOrgClass() == 3) {
                all = webOrgRepository.findAll(QWebOrg.webOrg.id.eq(tlongUser1.getOrgId()));
            }else {
                all = webOrgRepository.findAll(QWebOrg.webOrg.parentOrgId.eq(tlongUser1.getOrgId()));
            }
            if (Objects.nonNull(all)){
                List<WebOrg> webOrgs = ToListUtil.IterableToList(all);
                List<Long> orgIds = webOrgs.stream().map(WebOrg::getId).collect(Collectors.toList());
                //然后查询出这些机构下所有的代理商
                return appUserRepository.findAll(
                        tlongUser.orgId.in(orgIds)
                                .and(tlongUser.userType.eq(1)),pageRequest);
            }else {
                //当前机构下没任何机构 返回空
                return null;
            }
        }
        return null;
    }


    /**
     * 查询所有供应商
     */
    public Page<TlongUserResponseDto> findAllSuppliers(PageAndSortRequestDto requestDto, Long userId, MultiValueMap<String,String> params) {
        TlongUser user = appUserRepository.findOne(userId);
        if (Objects.isNull(user)){
            throw new CustomException("当前用户不存在!");
        }

        //处理模糊查询
        Predicate[] resove = resove(params);
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);

        //获取所有的供应商当前用户的机构下面的
        Page<TlongUser> users = appUserRepository.findAll(tlongUser.userType.eq(0)
                .and(tlongUser.orgId.eq(user.getOrgId()))
                .and(resove[0]), pageRequest);

        Map<Long, String> orgNameMap = this.orgNameMap(users);

        return users.map(one -> {
            TlongUserResponseDto response = new TlongUserResponseDto();
            response.setId(one.getId());
            response.setRegistDate(one.getRegistDate());
            response.setUserName(one.getUserName());
            response.setUserType(one.getUserType());
            response.setIsCompany(one.getIsCompany());
            response.setRealName(one.getRealName());
            response.setBirthday(one.getBirthday());
            response.setSex(one.getSex());
            response.setWx(one.getWx());
            response.setNickName(one.getNickName());
            response.setEsgin(one.getEsgin());
            response.setOrgName(orgNameMap.get(one.getOrgId()));
            response.setAuthentication(one.getAuthentication());
            response.setPremises(one.getPremises());
            response.setWx(one.getWx());
            response.setUserCode(one.getUserCode());
            return response;
        });
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
        registerRequsetDto.setOrgName(one.getOrgName());
        registerRequsetDto.setNickName(tlongUser.getNickName());
        registerRequsetDto.setIsExemption(tlongUser.getIsExemption());
        registerRequsetDto.setUserCode(tlongUser.getUserCode());
        registerRequsetDto.setGoodsClass(tlongUser.getGoodsClass());
        return registerRequsetDto;
    }

    public Result update(SuppliersRegisterRequsetDto requsetDto) {
//        WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requsetDto.getOrgId()));
//        if (one1==null) {
//            return new Result(0, "机构不存在");
//        }
        TlongUser tlongUser2;
        TlongUser tlongUser = appUserRepository.findOne(requsetDto.getId());
        if (requsetDto.getIsApp() == 1){
            TlongUser tlongUser1 = setValues(tlongUser, requsetDto);
            tlongUser2 = appUserRepository.save(tlongUser1);
        }else {
//        tlongUser.setId(requsetDto.getId());
            tlongUser.setArea(requsetDto.getArea());
            tlongUser.setUserName(requsetDto.getUserName());
            tlongUser.setPassword(requsetDto.getPassword());
            tlongUser.setUserType(requsetDto.getUserType());
            tlongUser.setIsCompany(requsetDto.getIsCompany());
            tlongUser.setIdcardNumber(requsetDto.getIdcardNumber());
            tlongUser.setRealName(requsetDto.getRealName());
            tlongUser.setBirthday(requsetDto.getBirthday());
            tlongUser.setSex(requsetDto.getSex());
            WebOrg one = webOrgRepository.findOne(QWebOrg.webOrg.orgName.eq(requsetDto.getOrgName()));
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
            if (requsetDto.getHeadImage() != null && !requsetDto.getHeadImage().equals("")) {
                tlongUser.setHeadImage(requsetDto.getHeadImage());
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
//            tlongUser.setOrgId(one1.getId());
            tlongUser.setNickName(requsetDto.getNickName());
            tlongUser.setIsExemption(requsetDto.getIsExemption());
            tlongUser.setPhone(requsetDto.getPhone());
            tlongUser2 = appUserRepository.save(tlongUser);
        }

        if (tlongUser2 == null) {
            return new Result(0, "修改失败");
        }
        return new Result(1, "修改成功");

    }

    private TlongUser setValues(TlongUser tlongUser, SuppliersRegisterRequsetDto requsetDto ){
        PropertyUtils.copyPropertiesOfNotNull(requsetDto,tlongUser);
        return tlongUser;
    }

    /**
     * 查询所有代理商
     */
    public Page<TlongUserResponseDto> findAllAgents(PageAndSortRequestDto pageAndSortRequestDto, Long userId) {
        TlongUser user = appUserRepository.findOne(userId);
        if (Objects.isNull(user)){
            throw new CustomException("当前用户不存在!");
        }
        //处理分页信息
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);

        Page<TlongUser> users = this.childrenUserIds(user, pageRequest);
        Map<Long, String> orgNameMap = this.orgNameMap(users);

        return users.map(one -> {
            TlongUserResponseDto registerRequsetDto = new TlongUserResponseDto();
            registerRequsetDto.setId(one.getId());
            registerRequsetDto.setUserName(one.getUserName());
            registerRequsetDto.setUserType(one.getUserType());
            registerRequsetDto.setIsCompany(one.getIsCompany());
            registerRequsetDto.setOrgId(one.getOrgId());
            registerRequsetDto.setOrgName(orgNameMap.get(one.getOrgId()));
            registerRequsetDto.setRealName(one.getRealName());
            registerRequsetDto.setBirthday(one.getBirthday());
            registerRequsetDto.setSex(one.getSex());
            registerRequsetDto.setWx(one.getWx());
            registerRequsetDto.setNickName(one.getNickName());
            registerRequsetDto.setEsgin(one.getEsgin());
            registerRequsetDto.setAuthentication(one.getAuthentication());
            registerRequsetDto.setRegistDate(one.getRegistDate());
            registerRequsetDto.setUserCode(one.getUserCode());
            return registerRequsetDto;
        });
    }


    /**
     * 修改认证状态
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
     * 查询发布商品数量 重新发布数量
     */
    public FindUserPublishNumResponseDto findUserPublishNumm(Long id, Integer isCompany) {
        if (isCompany != null){
            TlongUserSettings defaultSettings = tlongUserSettingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.eq(isCompany)
                    .and(QTlongUserSettings.tlongUserSettings.userId.isNull()));
            switch (isCompany){
                case 0 :
                    TlongUserSettings one = tlongUserSettingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.eq(0)
                            .and(QTlongUserSettings.tlongUserSettings.userId.eq(id)));
                    if (Objects.isNull(one)){
                        return new FindUserPublishNumResponseDto(defaultSettings.getGoodsReleaseNumber(),defaultSettings.getGoodsReReleaseNumber());
                    }
                    return new FindUserPublishNumResponseDto(one.getGoodsReleaseNumber(),one.getGoodsReReleaseNumber());
                case 1 :
                    TlongUserSettings one1 = tlongUserSettingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userType.eq(1)
                            .and(QTlongUserSettings.tlongUserSettings.userId.eq(id)));
                    if (Objects.isNull(one1)){
                        return new FindUserPublishNumResponseDto(defaultSettings.getGoodsReleaseNumber(),defaultSettings.getGoodsReReleaseNumber());
                    }
                    return new FindUserPublishNumResponseDto(one1.getGoodsReleaseNumber(),one1.getGoodsReReleaseNumber());
            }
        }
        TlongUser tlongUser = appUserRepository.findOne(id);

        TlongUserSettings one = tlongUserSettingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userId.eq(id)
                .and(QTlongUserSettings.tlongUserSettings.userType.eq(isCompany == null ? 0 : isCompany)));
        if (Objects.isNull(one)){
            if (null != tlongUser.getGoodsPublishNum()){
                return new FindUserPublishNumResponseDto(tlongUser.getGoodsPublishNum(),0);
            }
            return new FindUserPublishNumResponseDto(0,0);
        }
        return new FindUserPublishNumResponseDto(one.getGoodsReleaseNumber(),one.getGoodsReReleaseNumber());
    }

    /**
     * 修改发布商品数量 重新发布数量
     */
    public void updateUserPublishNumm(UpdateUserPublishNumRequsetDto requsetDto) {
        TlongUser tlongUser = appUserRepository.findOne(requsetDto.getUserId());
        tlongUser.setGoodsPublishNum(requsetDto.getPublishNumber());
        TlongUserSettings one = tlongUserSettingsRepository.findOne(QTlongUserSettings.tlongUserSettings.userId.eq(requsetDto.getUserId()));
        if (Objects.isNull(one)){
            //创建个人的发布数量重新发布数量
            TlongUserSettings tlongUserSettings = new TlongUserSettings();
            tlongUserSettings.setUserId(requsetDto.getUserId());
            tlongUserSettings.setUserType(requsetDto.getIsCompany());
            tlongUserSettings.setGoodsReleaseNumber(requsetDto.getPublishNumber());
            tlongUserSettings.setGoodsReReleaseNumber(requsetDto.getRePublishNumber());
            tlongUserSettings.setUserType(requsetDto.getIsCompany());
            tlongUserSettingsRepository.save(tlongUserSettings);
        }else {
            one.setGoodsReleaseNumber(requsetDto.getPublishNumber());
            one.setGoodsReReleaseNumber(requsetDto.getRePublishNumber());
            tlongUserSettingsRepository.save(one);
        }
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
//        //判断用户是否是管理员
//        if (user.getUserType() == null){
//
//        }
//        if (requestDto.getpType() == 2) {
//            pre = ExpressionUtils.and(pre, tlongUser.userType.isNotNull());
//            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getpType()));
//            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
//            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
//            final Predicate[] predicate = {tlongUser.id.isNull()};
//            all.forEach(one2 -> {
//                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
//            });
//            pre = ExpressionUtils.and(pre, predicate[0]);
//            pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
//        } else if (requestDto.getpType() == 5) {
//            if (user.getOrgId() != null) {
//                WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
//                Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
//                final Predicate[] predicate = {tlongUser.id.isNull()};
//                all.forEach(one2 -> {
//                    predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
//                });
//                pre = ExpressionUtils.and(pre, predicate[0]);
//                pre = ExpressionUtils.and(pre, tlongUser.id.longValue().ne(user.getId()));
//            }
//            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(2).or(tlongUser.userType.intValue().eq(3)).or(tlongUser.userType.intValue().eq(4)));
//        } else if (requestDto.getpType() == 1) {
//            if (user.getIsCompany() == null)
//                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
//            else if (user.getIsCompany() == 0)
//                pre = ExpressionUtils.and(pre, tlongUser.id.longValue().eq(user.getId()));
//            else {
//                pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(1));
//                pre = ExpressionUtils.and(pre, tlongUser.isCompany.intValue().eq(0));
//                pre = ExpressionUtils.and(pre, tlongUser.orgId.longValue().eq(webOrg.getId()));
//            }
//        } else {
//            pre = ExpressionUtils.and(pre, tlongUser.userType.intValue().eq(requestDto.getpType()));
//        }
//        if (StringUtils.isNotEmpty(requestDto.getUserName()))
//            pre = ExpressionUtils.and(pre, tlongUser.userName.eq(requestDto.getUserName()));
//        if (StringUtils.isNotEmpty(requestDto.getUserCode()))
//            pre = ExpressionUtils.and(pre, tlongUser.userCode.eq(requestDto.getUserCode()));
//        if (requestDto.getEsign() == 0 || requestDto.getEsign() == 1)
//            pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(requestDto.getEsign()));
//        if (requestDto.getAuthentication() == 0 || requestDto.getAuthentication() == 1)
//            pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(requestDto.getAuthentication()));
//        if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
//            pre = ExpressionUtils.and(pre, tlongUser.registDate.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
//        else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
//            pre = ExpressionUtils.and(pre, tlongUser.registDate.lt(requestDto.getEndTime() + " 23:59:59"));
//        else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
//            pre = ExpressionUtils.and(pre, tlongUser.registDate.gt(requestDto.getStartTime() + " 00:00:00"));
//        pre = ExpressionUtils.and(pre, tlongUser.esgin.intValue().eq(1));
//        pre = ExpressionUtils.and(pre, tlongUser.authentication.intValue().eq(1));
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
     */
    public Page<SuppliersCompanyResponseDto> findAgentByLevel(SuppliersCompanyRequestDto requestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);

        //根据级别查询出所有的代理商分公司
        Page<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgClass.eq(requestDto.getOrgClass()), pageRequest);
        return all.map(one ->{
            SuppliersCompanyResponseDto responseDto = new SuppliersCompanyResponseDto();
            responseDto.setOrgName(one.getOrgName());
            responseDto.setOrgId(one.getId());
            responseDto.setOrgClass(one.getOrgClass());
            WebOrg one1 = webOrgRepository.findOne(one.getParentOrgId());
            if (Objects.nonNull(one1)) {
                responseDto.setBelongToOrgName(one1.getOrgName());
            }
            responseDto.setRegistDate(one.getCreateDate() + "");
            return responseDto;
        });

    }

    public Page<SuppliersCompanyResponseDto> findSupplirtCompany(PageAndSortRequestDto requestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgClass.eq(4), pageRequest);
        return all.map(one ->{
            SuppliersCompanyResponseDto responseDto = new SuppliersCompanyResponseDto();
            responseDto.setOrgId(one.getId());
            responseDto.setOrgName(one.getOrgName());
            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getParentOrgId()));
            if (Objects.nonNull(one1)) {
                responseDto.setBelongToOrgName(one1.getOrgName());
            }
            responseDto.setRegistDate(one.getCreateDate() + "");
            return responseDto;
        });

//        Page<TlongUser> tlongUsers = appUserRepository.findAll(tlongUser.isCompany.intValue().eq(1), pageRequest);
//        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
//        final int[] orderNumTotal = {0};
//        final double[] publishPriceTotal = {0.0};
//        final double[] founderPriceTotal = {0.0};
//        tlongUsers.forEach(one -> {
//            final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
//            final Predicate[] pre2 = {webOrder.id.isNull()};
//            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.isCompany.intValue().ne(2).and(tlongUser.orgId.eq(one.getOrgId())));
//            tlongUser3.forEach(two -> pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(two.getId())));
//            Iterable<WebGoods> appGoods1 = repository1.findAll(pre[0]);
//            List<Long> ids = new ArrayList<>();
//            appGoods1.forEach(goods -> ids.add(goods.getId()));
//            ids.forEach(three -> pre2[0] = ExpressionUtils.or(pre2[0], webOrder.goodsId.longValue().eq(three)));
//
//            final int[] orderNum = {0};
//            final double[] publishPrice = {0.0};
//            final double[] founderPrice = {0.0};
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//            if (requestDto.getCurrentMonth() != null) {
//                if (requestDto.getCurrentMonth() == 1)
//                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
//                Iterable<WebOrder> orders = repository.findAll(pre2[0]);
//                orders.forEach(order -> {
//                    final Predicate[] pre4 = {webOrder.id.isNotNull()};
//                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
//                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
//                    pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
//                    pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
//                    List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userCode, tlongUser.userName, tlongUser.userType, webGoods.goodsHead, webGoods.goodsPic, webGoods.publishUserId, webGoods.star,
//                            webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
//                            webOrder.state, webOrder.placeOrderTime)
//                            .from(tlongUser, webGoods, webOrder)
//                            .where(pre4[0])
//                            .fetch();
//                    tuples.forEach(three -> {
//                        if (three.get(webOrder.state) != null && three.get(webOrder.state) == 0) {
//                            orderNum[0]++;
//                            publishPrice[0] += three.get(webGoods.publishPrice);
//                            founderPrice[0] += three.get(webGoods.founderPrice);
//                            orderNumTotal[0]++;
//                            publishPriceTotal[0] += three.get(webGoods.publishPrice);
//                            founderPriceTotal[0] += three.get(webGoods.founderPrice);
//                        }
//                    });
//                });
//            }
//            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
//            registerRequsetDto.setId(one.getId());
//            registerRequsetDto.setUserName(one.getUserName());
//            registerRequsetDto.setPhone(one.getPhone());
//            WebOrg one1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
//            registerRequsetDto.setOrgId(one1.getOrgName());
//            registerRequsetDto.setRegistDate(one.getRegistDate());
//            registerRequsetDto.setRoleName("供应商分公司");
//            registerRequsetDto.setOrderNum(orderNum[0]);
//            registerRequsetDto.setPublishPrice(publishPrice[0]);
//            suppliersRegisterRequsetDtos.add(registerRequsetDto);
//        });
//        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
//        final int[] count = {0};
//        Iterable<TlongUser> tlongUsers1 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().eq(2)));
//        tlongUsers1.forEach(tlongUser -> {
//            count[0]++;
//        });
//        pageSuppliersResponseDto.setCount(count[0]);
//        pageSuppliersResponseDto.setOrderNum(orderNumTotal[0]);
//        pageSuppliersResponseDto.setPublishPrice(publishPriceTotal[0]);
//        return pageSuppliersResponseDto;
    }

    /**
     * 查询高级管理员 TODO 前端调整
     */
    public Page<SuppliersRegisterRequsetDto> findAllManager(PageAndSortRequestDto requestDto) {
//        PageResponseDto<SuppliersRegisterRequsetDto> pageSuppliersResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<TlongUserRole> tlongUserRoles = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(6).or(tlongUserRole.roleId.intValue().eq(9)), pageRequest);
//        List<SuppliersRegisterRequsetDto> suppliersRegisterRequsetDtos = new ArrayList<>();
        return tlongUserRoles.map(one -> {
            SuppliersRegisterRequsetDto registerRequsetDto = new SuppliersRegisterRequsetDto();
            registerRequsetDto.setRoleName(tlongRoleRepository.findOne(one.getRoleId()).getRoleName());
            TlongUser tlongUser = appUserRepository.findOne(one.getUserId());
            registerRequsetDto.setRoleId(one.getRoleId());
            registerRequsetDto.setId(tlongUser.getId());
            registerRequsetDto.setUserName(tlongUser.getUserName());
            registerRequsetDto.setRealName(tlongUser.getRealName());
            registerRequsetDto.setRegistDate(tlongUser.getRegistDate());
//            suppliersRegisterRequsetDtos.add(registerRequsetDto);
            return registerRequsetDto;
        });
//        pageSuppliersResponseDto.setList(suppliersRegisterRequsetDtos);
//        Iterable<TlongUserRole> tlongUserRoles1 = tlongUserRoleRepository.findAll(tlongUserRole.roleId.intValue().eq(6).or(tlongUserRole.roleId.intValue().eq(9)));
//        final int[] count = {0};
//        tlongUserRoles1.forEach(tlongUserRole -> {
//            count[0]++;
//        });
//        pageSuppliersResponseDto.setCount(count[0]);
//        return pageSuppliersResponseDto;
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
                registerRequsetDto.setOrgName(one1.getOrgName());
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
                    registerRequsetDto.setOrgName(one1.getOrgName());
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
            registerRequsetDto.setOrgName(one1.getOrgName());
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
            registerRequsetDto.setOrgName(one1.getOrgName());
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
            registerRequsetDto.setOrgName(one1.getOrgName());
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

    /**
     * 获取所有下级代理商
     */
    public Page<AgentResponseDto> childrenAgents(Long userId, PageAndSortRequestDto pageAndSortRequestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);
        Page<TlongUser> all = appUserRepository.findAll(tlongUser.parentId.eq(userId), pageRequest);
        return all.map(one -> {
            AgentResponseDto responseDto = new AgentResponseDto();
            WebOrg webOrg = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(one.getOrgId()));
            responseDto.setOrgId(webOrg.getOrgName());
            responseDto.setAuthentication(one.getAuthentication());
            responseDto.setBirthday(one.getBirthday());
            responseDto.setEsgin(one.getEsgin());
            responseDto.setId(one.getId());
            responseDto.setRegistDate(one.getRegistDate());
            responseDto.setSex(one.getSex());
            responseDto.setUserCode(one.getUserCode());
            responseDto.setUserName(one.getUserName());
            responseDto.setUserType(one.getUserType());
            responseDto.setRealName(one.getRealName());
            responseDto.setWx(one.getWx());
            return responseDto;
        });
    }


    /**
     * 用户多条件模糊查询
     */
    public Predicate[] resove(MultiValueMap<String,String> params) {

        String esgin = params.getFirst("esgin");
        String userName = params.getFirst("userName");
        String userCode = params.getFirst("userCode");
        String authentication = params.getFirst("authentication");
        String userType = params.getFirst("pType");
        String registDate = params.getFirst("registDate");

        BooleanExpression userTypeEq = StringUtils.isNotEmpty(userType) ? QTlongUser.tlongUser.userType.eq(Integer.valueOf(userType)) : null;
        BooleanExpression esginEq = StringUtils.isNotEmpty(esgin) ? QTlongUser.tlongUser.esgin.eq(Integer.valueOf(esgin)) : null;
        BooleanExpression userNameEq = StringUtils.isNotEmpty(userName) ? QTlongUser.tlongUser.userName.like(userName) : null;
        BooleanExpression userCodeEq = StringUtils.isNotEmpty(userCode) ? QTlongUser.tlongUser.userCode.like("%" + userCode + "%") : null;
        BooleanExpression  authenticationEq = StringUtils.isNotEmpty(authentication) ? QTlongUser.tlongUser.authentication.eq(Integer.valueOf(authentication)) : null;
        BooleanExpression registDateEq = StringUtils.isNotEmpty(registDate) ? QTlongUser.tlongUser.registDate.like(registDate) : null;

        List<BooleanExpression> list = new ArrayList<>();
        list.add(userTypeEq);
        list.add(esginEq);
        list.add(userNameEq);
        list.add(userCodeEq);
        list.add(authenticationEq);
        list.add(registDateEq);

        List<BooleanExpression> collect = list.stream().filter(Objects::nonNull).collect(Collectors.toList());
        Predicate[] pre = {QTlongUser.tlongUser.id.isNotNull()};
        collect.forEach(one -> pre[0] = ExpressionUtils.and(pre[0], one));
        return pre;
    }


}

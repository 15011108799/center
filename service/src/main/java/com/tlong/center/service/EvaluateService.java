package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.api.dto.web.EvaluateRequestDto;
import com.tlong.center.api.dto.web.EvaluateSearchRequestDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.*;
import com.tlong.center.domain.web.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
import static com.tlong.center.domain.web.QTlongUserRole.tlongUserRole;
import static com.tlong.center.domain.web.QWebEvaluate.webEvaluate;
import static com.tlong.center.domain.web.QWebOrder.webOrder;

@Component
@Transactional
public class EvaluateService {
    final WebEvaluateRepository repository;
    final EntityManager entityManager;
    private final AppUserRepository appUserRepository;
    private final GoodsRepository repository1;
    private final TlongUserRoleRepository tlongUserRoleRepository;
    private final WebOrgRepository webOrgRepository;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public EvaluateService(EntityManager entityManager, WebEvaluateRepository repository, AppUserRepository appUserRepository, GoodsRepository repository1, TlongUserRoleRepository tlongUserRoleRepository,WebOrgRepository webOrgRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.repository1 = repository1;
        this.tlongUserRoleRepository = tlongUserRoleRepository;
        this.webOrgRepository=webOrgRepository;
    }

    /**
     * 分页查询评价列表
     */
    public PageResponseDto<EvaluateRequestDto> findAllEvaluate(PageAndSortRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        Iterable<WebGoods> appGoods1;
        if (user.getUserType() != null && user.getUserType() == 1) {
            if (user.getIsCompany() == 0 || user.getIsCompany() == 1)
                appGoods1 = repository1.findAll(QWebGoods.webGoods.publishUserId.longValue().eq(user.getId()));
            else {
                final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
                Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
                tlongUser3.forEach(one -> {
                    pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
                });
                appGoods1 = repository1.findAll(pre[0]);
            }
        } else
            appGoods1 = repository1.findAll();
        List<Long> ids = new ArrayList<>();
        appGoods1.forEach(goods -> {
            ids.add(goods.getId());
        });
        PageResponseDto<EvaluateRequestDto> evaluateRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebEvaluate> evaluates;
        final Predicate[] pre = {webEvaluate.id.isNull()};
        final Predicate[] pre2 = {webEvaluate.id.isNull()};
        ids.forEach(one -> {
            pre[0] = ExpressionUtils.or(pre[0], webEvaluate.goodsId.longValue().eq(one));
        });
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            Predicate predicate1=tlongUser.id.isNotNull();
            predicate1=ExpressionUtils.and(predicate1,tlongUser.userType.intValue().eq(2));
            predicate1=ExpressionUtils.and(predicate1,tlongUser.orgId.isNotNull());
            predicate1=ExpressionUtils.and(predicate1,predicate[0]);
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate1);
            tlongUser3.forEach(one -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webEvaluate.userId.longValue().eq(one.getId()));
            });
            evaluates = repository.findAll(pre2[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre2[0] = ExpressionUtils.or(pre2[0], webEvaluate.userId.longValue().eq(one.getId()));
                });
                evaluates = repository.findAll(pre2[0], pageRequest);
            } else {
                evaluates = repository.findAll(pre[0], pageRequest);
            }
        }
        List<EvaluateRequestDto> requestDtos = new ArrayList<>();
        evaluates.forEach(evaluate -> {
            List<Tuple> tuples = queryFactory.select(webEvaluate.id, webGoods.goodsHead, webGoods.goodsCode, webGoods.goodsPic, webEvaluate.content, webEvaluate.pics, webEvaluate.star, webEvaluate.time)
                    .from(webEvaluate, webGoods)
                    .where(webGoods.id.eq(webEvaluate.goodsId).and(webGoods.id.longValue().eq(evaluate.getGoodsId())))
                    .fetch();
            tuples.stream().forEach(one -> {
                EvaluateRequestDto evaluateRequestDto = new EvaluateRequestDto();
                evaluateRequestDto.setId(one.get(webEvaluate.id));
                evaluateRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                evaluateRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                evaluateRequestDto.setGoodsUrls(one.get(webGoods.goodsPic));
                evaluateRequestDto.setContent(one.get(webEvaluate.content));
                evaluateRequestDto.setPics(one.get(webEvaluate.pics));
                evaluateRequestDto.setStar(one.get(webEvaluate.star));
                evaluateRequestDto.setTime(one.get(webEvaluate.time));
                requestDtos.add(evaluateRequestDto);
            });
        });
        evaluateRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        final Predicate[] pre1 = {webEvaluate.id.isNull()};
        ids.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], webEvaluate.goodsId.longValue().eq(one));
        });
        Iterable<WebEvaluate> evaluates1;
        final Predicate[] pre3 = {webEvaluate.id.isNull()};
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            Predicate predicate1=tlongUser.id.isNotNull();
            predicate1=ExpressionUtils.and(predicate1,tlongUser.userType.intValue().eq(2));
            predicate1=ExpressionUtils.and(predicate1,tlongUser.orgId.isNotNull());
            predicate1=ExpressionUtils.and(predicate1,predicate[0]);
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate1);            tlongUser3.forEach(one -> {
                pre3[0] = ExpressionUtils.or(pre3[0], webEvaluate.userId.longValue().eq(one.getId()));
            });
            evaluates1 = repository.findAll(pre3[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre3[0] = ExpressionUtils.or(pre3[0], webEvaluate.userId.longValue().eq(one.getId()));
                });
                evaluates1 = repository.findAll(pre3[0]);
            } else
                evaluates1 = repository.findAll(pre[0]);
        }
        evaluates1.forEach(order -> {
            count[0]++;
        });
        evaluateRequestDtoPageResponseDto.setCount(count[0]);
        return evaluateRequestDtoPageResponseDto;
    }

    public PageResponseDto<EvaluateRequestDto> searchEvaluate(EvaluateSearchRequestDto requestDto, HttpSession session) {
        TlongUser user = (TlongUser) session.getAttribute("tlongUser");
        PageResponseDto<EvaluateRequestDto> evaluateRequestDtoPageResponseDto = new PageResponseDto<>();
        List<EvaluateRequestDto> requestDtos = new ArrayList<>();
        Iterable<WebGoods> appGoods1;
        if (user.getUserType() != null && user.getUserType() == 1) {
            if (user.getIsCompany() == 0 || user.getIsCompany() == 1)
                appGoods1 = repository1.findAll(QWebGoods.webGoods.publishUserId.longValue().eq(user.getId()));
            else {
                final Predicate[] pre = {QWebGoods.webGoods.id.isNull()};
                Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(tlongUser.userType.intValue().eq(1).and(tlongUser.isCompany.intValue().ne(2)).and(tlongUser.orgId.isNotNull()).and(tlongUser.orgId.eq(user.getOrgId())));
                tlongUser3.forEach(one -> {
                    pre[0] = ExpressionUtils.or(pre[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
                });
                appGoods1 = repository1.findAll(pre[0]);
            }
        } else
            appGoods1 = repository1.findAll();
        List<Long> ids = new ArrayList<>();
        if (StringUtils.isNotEmpty(requestDto.getGoodsCode())) {
            WebGoods appGoods = repository1.findOne(webGoods.goodsCode.eq(requestDto.getGoodsCode()));
            if (appGoods != null)
                ids.add(appGoods.getId());
            else {
                evaluateRequestDtoPageResponseDto.setList(requestDtos);
                evaluateRequestDtoPageResponseDto.setCount(0);
                return evaluateRequestDtoPageResponseDto;
            }
        } else {
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
        }

        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<WebEvaluate> evaluates;
        final Predicate[] pre = {webEvaluate.id.isNull()};
        final Predicate[] pre2 = {webEvaluate.id.isNull()};
        ids.forEach(one -> {
            pre[0] = ExpressionUtils.or(pre[0], webEvaluate.goodsId.longValue().eq(one));
        });
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            Predicate predicate1=tlongUser.id.isNotNull();
            predicate1=ExpressionUtils.and(predicate1,tlongUser.userType.intValue().eq(2));
            predicate1=ExpressionUtils.and(predicate1,tlongUser.orgId.isNotNull());
            predicate1=ExpressionUtils.and(predicate1,predicate[0]);
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate1);            tlongUser3.forEach(one -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webEvaluate.userId.longValue().eq(one.getId()));
            });
            pre2[0] = ExpressionUtils.and(pre2[0], pre[0]);
            evaluates = repository.findAll(pre2[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre2[0] = ExpressionUtils.or(pre2[0], webEvaluate.userId.longValue().eq(one.getId()));
                });
                pre2[0] = ExpressionUtils.and(pre2[0], pre[0]);
                evaluates = repository.findAll(pre2[0], pageRequest);
            } else
                evaluates = repository.findAll(pre[0], pageRequest);
        }

        evaluates.forEach(evaluate -> {
            final Predicate[] pre4 = {webEvaluate.id.isNotNull()};
            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webEvaluate.goodsId));
            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(evaluate.getGoodsId()));
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre4[0] = ExpressionUtils.and(pre4[0], webEvaluate.time.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre4[0] = ExpressionUtils.and(pre4[0], webEvaluate.time.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre4[0] = ExpressionUtils.and(pre4[0], webEvaluate.time.gt(requestDto.getStartTime() + " 00:00:00"));
            List<Tuple> tuples = queryFactory.select(webEvaluate.id, webGoods.goodsHead, webGoods.goodsCode, webGoods.goodsPic, webEvaluate.content, webEvaluate.pics, webEvaluate.star, webEvaluate.time)
                    .from(webEvaluate, webGoods)
                    .where(pre4[0])
                    .fetch();
            tuples.stream().forEach(one -> {
                EvaluateRequestDto evaluateRequestDto = new EvaluateRequestDto();
                evaluateRequestDto.setId(one.get(webEvaluate.id));
                evaluateRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                evaluateRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                evaluateRequestDto.setGoodsUrls(one.get(webGoods.goodsPic));
                evaluateRequestDto.setContent(one.get(webEvaluate.content));
                evaluateRequestDto.setPics(one.get(webEvaluate.pics));
                evaluateRequestDto.setStar(one.get(webEvaluate.star));
                evaluateRequestDto.setTime(one.get(webEvaluate.time));
                requestDtos.add(evaluateRequestDto);
            });
        });
        evaluateRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        final Predicate[] pre1 = {webEvaluate.id.isNull()};
        ids.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], webEvaluate.goodsId.longValue().eq(one));
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.gt(requestDto.getStartTime() + " 00:00:00"));
        });
        Iterable<WebEvaluate> evaluates1;
        final Predicate[] pre3 = {webEvaluate.id.isNull()};
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            Predicate predicate1=tlongUser.id.isNotNull();
            predicate1=ExpressionUtils.and(predicate1,tlongUser.userType.intValue().eq(2));
            predicate1=ExpressionUtils.and(predicate1,tlongUser.orgId.isNotNull());
            predicate1=ExpressionUtils.and(predicate1,predicate[0]);
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate1);            tlongUser3.forEach(one -> {
                pre3[0] = ExpressionUtils.or(pre3[0], webEvaluate.userId.longValue().eq(one.getId()));
                if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                    pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
                else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                    pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.lt(requestDto.getEndTime() + " 23:59:59"));
                else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                    pre1[0] = ExpressionUtils.and(pre1[0], webEvaluate.time.gt(requestDto.getStartTime() + " 00:00:00"));
            });
            pre3[0] = ExpressionUtils.and(pre3[0], pre1[0]);
            evaluates1 = repository.findAll(pre3[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre3[0] = ExpressionUtils.or(pre3[0], webEvaluate.userId.longValue().eq(one.getId()));
                });
                pre3[0] = ExpressionUtils.and(pre3[0], pre1[0]);
                evaluates1 = repository.findAll(pre3[0]);
            } else
                evaluates1 = repository.findAll(pre[0]);
        }
        evaluates1.forEach(order -> {
            count[0]++;
        });
        evaluateRequestDtoPageResponseDto.setCount(count[0]);
        return evaluateRequestDtoPageResponseDto;
    }
}

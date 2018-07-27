package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.order.OrderSearchRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
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
public class OrderService {
    final OrderRepository repository;
    final EntityManager entityManager;
    final AppUserRepository appUserRepository;
    final GoodsRepository repository1;
    final TlongUserRoleRepository tlongUserRoleRepository;
    final WebOrgRepository webOrgRepository;
    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public OrderService(EntityManager entityManager, OrderRepository repository, AppUserRepository appUserRepository, GoodsRepository repository1, TlongUserRoleRepository tlongUserRoleRepository,WebOrgRepository webOrgRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository = appUserRepository;
        this.repository1 = repository1;
        this.tlongUserRoleRepository = tlongUserRoleRepository;
        this.webOrgRepository=webOrgRepository;
    }

    /**
     * 分页查询订单列表
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<OrderRequestDto> findAllOrders(PageAndSortRequestDto requestDto, HttpSession session) {
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
        } else {
            appGoods1 = repository1.findAll();
        }
        List<Long> ids = new ArrayList<>();
        appGoods1.forEach(goods -> {
            ids.add(goods.getId());
        });
        PageResponseDto<OrderRequestDto> orderRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebOrder> orders;
        final Predicate[] pre = {webOrder.id.isNull()};
        final Predicate[] pre2 = {webOrder.id.isNull()};
        ids.forEach(one -> {
            pre[0] = ExpressionUtils.or(pre[0], webOrder.goodsId.longValue().eq(one));
        });
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        if (requestDto.getCurrentMonth() == 1)
            pre[0] = ExpressionUtils.and(pre[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
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
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(one.getId()));
            });
            if (requestDto.getCurrentMonth() == 1)
                pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
            orders = repository.findAll(pre2[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                all.forEach(one -> {
                    pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(one.getId()));
                });
                if (requestDto.getCurrentMonth() == 1)
                    pre2[0] = ExpressionUtils.and(pre2[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
                orders = repository.findAll(pre2[0], pageRequest);
            } else {
                orders = repository.findAll(pre[0], pageRequest);
            }
        }
        final int[] orderNum = {0};
        final double[] publishPrice = {0.0};
        final double[] founderPrice = {0.0};
        List<OrderRequestDto> requestDtos = new ArrayList<>();
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

            tuples.stream().forEach(one -> {
                Integer userType = one.get(tlongUser.userType);
              /*  if (one.get(webOrder.state) != null && one.get(webOrder.state) == 0) {
                    orderNum[0]++;
                    publishPrice[0] += one.get(webGoods.publishPrice);
                    founderPrice[0] += one.get(webGoods.founderPrice);
                }*/
                OrderRequestDto orderRequestDto = new OrderRequestDto();
                orderRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                orderRequestDto.setUserName(one.get(tlongUser.userName));
                orderRequestDto.setRealName(one.get(tlongUser.realName));
                orderRequestDto.setUserCode(one.get(tlongUser.userCode));
                orderRequestDto.setUserType(one.get(tlongUser.userType));
                orderRequestDto.setGoodsUrl(one.get(webGoods.goodsPic).split(",")[0]);
                orderRequestDto.setState(one.get(webOrder.state));
                orderRequestDto.setUserPhone(one.get(tlongUser.phone));
                orderRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                orderRequestDto.setGoodsStar(one.get(webGoods.star));
                orderRequestDto.setFounderPrice(one.get(webGoods.founderPrice));
                orderRequestDto.setPublishPrice(one.get(webGoods.publishPrice));
                orderRequestDto.setGoodsPrice(userType == null ? 0.0 : userType == 2 ? one.get(webGoods.founderPrice) : userType == 3 ? one.get(webGoods.flagshipPrice) : one.get(webGoods.storePrice));
                orderRequestDto.setPlaceOrderTime(one.get(webOrder.placeOrderTime));
                if (one.get(webGoods.publishUserId) != null) {
                    TlongUser tlongUser = appUserRepository.findOne(one.get(webGoods.publishUserId));
                    orderRequestDto.setPublishName(tlongUser.getRealName());
                    orderRequestDto.setPublishPhone(tlongUser.getPhone());
                    orderRequestDto.setPublishCode(tlongUser.getUserCode());
                }
                requestDtos.add(orderRequestDto);
            });
        });
        orderRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        final Predicate[] pre1 = {webOrder.id.isNull()};
        final Predicate[] pre3 = {webOrder.id.isNull()};
        ids.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], webOrder.goodsId.longValue().eq(one));
        });
        if (requestDto.getCurrentMonth() == 1)
            pre1[0] = ExpressionUtils.and(pre1[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
        Iterable<WebOrder> orders1;
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            predicate[0]=ExpressionUtils.and(predicate[0],tlongUser.userType.intValue().eq(2));
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate[0]);
            tlongUser3.forEach(one -> {
                pre3[0] = ExpressionUtils.or(pre3[0], webOrder.userId.longValue().eq(one.getId()));
            });
            if (requestDto.getCurrentMonth() == 1)
                pre3[0] = ExpressionUtils.and(pre3[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
            orders1 = repository.findAll(pre3[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre3[0] = ExpressionUtils.or(pre3[0], webOrder.userId.longValue().eq(one.getId()));
                });
                if (requestDto.getCurrentMonth() == 1)
                    pre3[0] = ExpressionUtils.and(pre3[0], webOrder.placeOrderTime.between(sdf.format(new Date()) + "-01 00:00:00", sdf.format(new Date()) + "-31 23:59:59"));
                orders1 = repository.findAll(pre3[0]);
            } else {
                orders1 = repository.findAll(pre1[0]);
            }
        }
        orders1.forEach(order -> {
                    count[0]++;
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

                    tuples.stream().forEach(one -> {
                        if (one.get(webOrder.state) != null && one.get(webOrder.state) == 0) {
                            orderNum[0]++;
                            publishPrice[0] += one.get(webGoods.publishPrice);
                            founderPrice[0] += one.get(webGoods.founderPrice);
                        }
                    });
                });
        orderRequestDtoPageResponseDto.setCount(count[0]);
        orderRequestDtoPageResponseDto.setOrderNum(orderNum[0]);
        orderRequestDtoPageResponseDto.setFounderPrice(founderPrice[0]);
        orderRequestDtoPageResponseDto.setPublishPrice(publishPrice[0]);
        return orderRequestDtoPageResponseDto;
    }

    public PageResponseDto<OrderRequestDto> searchOrders(OrderSearchRequestDto requestDto, HttpSession session) {
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
        } else {
            final Predicate[] pre = {tlongUser.id.isNotNull()};
            final Predicate[] pre1 = {QWebGoods.webGoods.id.isNull()};
            if (StringUtils.isNotEmpty(requestDto.getPublishCode()))
                pre[0] = ExpressionUtils.and(pre[0], tlongUser.userCode.eq(requestDto.getPublishCode()));
            Iterable<TlongUser> all = appUserRepository.findAll(pre[0]);
            all.forEach(one -> {
                pre1[0] = ExpressionUtils.or(pre1[0], QWebGoods.webGoods.publishUserId.longValue().eq(one.getId()));
            });
            appGoods1 = repository1.findAll(pre1[0]);
        }
        List<Long> ids = new ArrayList<>();
        PageResponseDto<OrderRequestDto> orderRequestDtoPageResponseDto = new PageResponseDto<>();
        List<OrderRequestDto> requestDtos = new ArrayList<>();
        if (StringUtils.isNotEmpty(requestDto.getGoodsCode())) {
            TlongUser one = null;
            WebGoods appGoods = null;
            if (StringUtils.isNotEmpty(requestDto.getPublishCode())) {
                one = appUserRepository.findOne(tlongUser.userCode.eq(requestDto.getPublishCode()));
                if (one == null)
                    appGoods = null;
                else
                    appGoods = repository1.findOne(webGoods.goodsCode.eq(requestDto.getGoodsCode()).and(webGoods.publishUserId.longValue().eq(one.getId())));
            } else
                appGoods = repository1.findOne(webGoods.goodsCode.eq(requestDto.getGoodsCode()));
            if (appGoods != null)
                ids.add(appGoods.getId());
            else {
                orderRequestDtoPageResponseDto.setList(requestDtos);
                orderRequestDtoPageResponseDto.setCount(0);
                return orderRequestDtoPageResponseDto;
            }
        } else {
            appGoods1.forEach(goods -> {
                ids.add(goods.getId());
            });
        }
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto.getPageAndSortRequestDto());
        Page<WebOrder> orders;
        final Predicate[] pre = {webOrder.id.isNull()};
        final Predicate[] pre2 = {webOrder.id.isNull()};
        ids.forEach(one -> {
            pre[0] = ExpressionUtils.or(pre[0], webOrder.goodsId.longValue().eq(one));
        });
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            predicate[0]=ExpressionUtils.and(predicate[0],tlongUser.userType.intValue().eq(2));
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate[0]);
            tlongUser3.forEach(one -> {
                pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(one.getId()));
            });
            pre2[0] = ExpressionUtils.and(pre2[0], pre[0]);
            orders = repository.findAll(pre2[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);                all.forEach(one -> {
                    pre2[0] = ExpressionUtils.or(pre2[0], webOrder.userId.longValue().eq(one.getId()));
                });
                pre2[0] = ExpressionUtils.and(pre2[0], pre[0]);
                orders = repository.findAll(pre2[0], pageRequest);
            } else {
                Predicate preUser = tlongUser.id.isNotNull();
                final Predicate[] pre5 = {webOrder.id.isNull()};
                if (StringUtils.isNotEmpty(requestDto.getPlaceUserName()))
                    preUser = ExpressionUtils.and(preUser, tlongUser.userName.eq(requestDto.getPlaceUserName()));
                if (StringUtils.isNotEmpty(requestDto.getPlaceUsesrCode()))
                    preUser = ExpressionUtils.and(preUser, tlongUser.userCode.eq(requestDto.getPlaceUsesrCode()));
                Iterable<TlongUser> all = appUserRepository.findAll(preUser);
                all.forEach(one -> {
                    pre5[0] = ExpressionUtils.or(pre5[0], webOrder.userId.longValue().eq(one.getId()));
                });
                pre[0] = ExpressionUtils.and(pre[0], pre5[0]);
                orders = repository.findAll(pre[0], pageRequest);
            }
        }
        final int[] orderNum = {0};
        final double[] publishPrice = {0.0};
        final double[] founderPrice = {0.0};
        orders.forEach(order -> {
            final Predicate[] pre4 = {webOrder.id.isNotNull()};
            pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.eq(webOrder.userId));
            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.eq(webOrder.goodsId));
            pre4[0] = ExpressionUtils.and(pre4[0], tlongUser.id.longValue().eq(order.getUserId()));
            pre4[0] = ExpressionUtils.and(pre4[0], webGoods.id.longValue().eq(order.getGoodsId()));
            if (requestDto.getOrderState() != 3)
                pre4[0] = ExpressionUtils.and(pre4[0], webOrder.state.intValue().eq(requestDto.getOrderState()));
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
            tuples.stream().forEach(one -> {
                Integer userType = one.get(tlongUser.userType);
                if (one.get(webOrder.state) != null && one.get(webOrder.state) == 0) {
                    orderNum[0]++;
                    publishPrice[0] += one.get(webGoods.publishPrice);
                    founderPrice[0] += one.get(webGoods.founderPrice);
                }
                OrderRequestDto orderRequestDto = new OrderRequestDto();
                orderRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                orderRequestDto.setUserName(one.get(tlongUser.userName));
                orderRequestDto.setRealName(one.get(tlongUser.realName));
                orderRequestDto.setUserCode(one.get(tlongUser.userCode));
                orderRequestDto.setUserType(one.get(tlongUser.userType));
                orderRequestDto.setGoodsUrl(one.get(webGoods.goodsPic).split(",")[0]);
                orderRequestDto.setState(one.get(webOrder.state));
                orderRequestDto.setUserPhone(one.get(tlongUser.phone));
                orderRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                orderRequestDto.setGoodsStar(one.get(webGoods.star));
                orderRequestDto.setFounderPrice(one.get(webGoods.founderPrice));
                orderRequestDto.setPublishPrice(one.get(webGoods.publishPrice));
                orderRequestDto.setGoodsPrice(userType == null ? 0.0 : userType == 2 ? one.get(webGoods.founderPrice) : userType == 3 ? one.get(webGoods.flagshipPrice) : one.get(webGoods.storePrice));
                orderRequestDto.setPlaceOrderTime(one.get(webOrder.placeOrderTime));
                if (one.get(webGoods.publishUserId) != null) {
                    TlongUser tlongUser = appUserRepository.findOne(one.get(webGoods.publishUserId));
                    orderRequestDto.setPublishName(tlongUser.getRealName());
                    orderRequestDto.setPublishPhone(tlongUser.getPhone());
                    orderRequestDto.setPublishCode(tlongUser.getUserCode());
                }
                requestDtos.add(orderRequestDto);
            });
        });
        orderRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        final Predicate[] pre1 = {webOrder.id.isNull()};
        final Predicate[] pre3 = {webOrder.id.isNull()};
        ids.forEach(one -> {
            pre1[0] = ExpressionUtils.or(pre1[0], webOrder.goodsId.longValue().eq(one));
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre1[0] = ExpressionUtils.and(pre1[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre1[0] = ExpressionUtils.and(pre1[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
        });
        Iterable<WebOrder> orders1;
        if (user.getUserType() != null && user.getUserType() != 1) {
            WebOrg webOrg1 = webOrgRepository.findOne(QWebOrg.webOrg.id.longValue().eq(user.getOrgId()));
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(webOrg1.getOrgName() + "%"));
            final Predicate[] predicate = {tlongUser.id.isNull()};
            all.forEach(one2 -> {
                predicate[0] = ExpressionUtils.or(predicate[0], tlongUser.orgId.longValue().eq(one2.getId()));
            });
            predicate[0]=ExpressionUtils.and(predicate[0],tlongUser.userType.intValue().eq(2));
            Iterable<TlongUser> tlongUser3 = appUserRepository.findAll(predicate[0]);
            tlongUser3.forEach(one -> {
                pre3[0] = ExpressionUtils.or(pre3[0], webOrder.userId.longValue().eq(one.getId()));
            });
            pre3[0] = ExpressionUtils.and(pre3[0], pre1[0]);
            if (requestDto.getStartTime() != null && requestDto.getEndTime() != null)
                pre3[0] = ExpressionUtils.and(pre3[0], webOrder.placeOrderTime.between(requestDto.getStartTime() + " 00:00:00", requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() == null && requestDto.getEndTime() != null)
                pre3[0] = ExpressionUtils.and(pre3[0], webOrder.placeOrderTime.lt(requestDto.getEndTime() + " 23:59:59"));
            else if (requestDto.getStartTime() != null && requestDto.getEndTime() == null)
                pre3[0] = ExpressionUtils.and(pre3[0], webOrder.placeOrderTime.gt(requestDto.getStartTime() + " 00:00:00"));
            orders1 = repository.findAll(pre3[0], pageRequest);
        } else {
            TlongUserRole tlongUserRole1 = tlongUserRoleRepository.findOne(tlongUserRole.userId.longValue().eq(user.getId()));
            if (tlongUserRole1.getRoleId() == 7 || tlongUserRole1.getRoleId() == 10 || tlongUserRole1.getRoleId() == 11) {
                Iterable<WebOrg> all1 = webOrgRepository.findAll(QWebOrg.webOrg.orgName.like(user.getUserName().substring(user.getUserName().indexOf('-') + 1, user.getUserName().length()) + "%"));
                Predicate[] predicates = {tlongUser.id.isNull()};
                all1.forEach(two -> {
                    predicates[0] = ExpressionUtils.or(predicates[0], tlongUser.orgId.longValue().eq(two.getId()));
                });
                Iterable<TlongUser> all = appUserRepository.findAll(predicates[0]);
                all.forEach(one -> {
                    pre3[0] = ExpressionUtils.or(pre3[0], webOrder.userId.longValue().eq(one.getId()));
                });
                pre3[0] = ExpressionUtils.and(pre3[0], pre1[0]);
                orders1 = repository.findAll(pre3[0]);
            } else {
                Predicate preUser = tlongUser.id.isNotNull();
                final Predicate[] pre5 = {webOrder.id.isNull()};
                if (StringUtils.isNotEmpty(requestDto.getPlaceUserName()))
                    preUser = ExpressionUtils.and(preUser, tlongUser.userName.eq(requestDto.getPlaceUserName()));
                if (StringUtils.isNotEmpty(requestDto.getPlaceUsesrCode()))
                    preUser = ExpressionUtils.and(preUser, tlongUser.userCode.eq(requestDto.getPlaceUsesrCode()));
                Iterable<TlongUser> all = appUserRepository.findAll(preUser);
                all.forEach(one -> {
                    pre5[0] = ExpressionUtils.or(pre5[0], webOrder.userId.longValue().eq(one.getId()));
                });
                pre1[0] = ExpressionUtils.and(pre1[0], pre5[0]);
                orders1 = repository.findAll(pre1[0]);
            }
        }
        orders1.forEach(order -> {
            count[0]++;
        });
        orderRequestDtoPageResponseDto.setCount(count[0]);
        orderRequestDtoPageResponseDto.setCount(count[0]);
        orderRequestDtoPageResponseDto.setOrderNum(orderNum[0]);
        orderRequestDtoPageResponseDto.setFounderPrice(founderPrice[0]);
        orderRequestDtoPageResponseDto.setPublishPrice(publishPrice[0]);
        return orderRequestDtoPageResponseDto;
    }
}

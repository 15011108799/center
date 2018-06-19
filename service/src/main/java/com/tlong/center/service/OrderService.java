package com.tlong.center.service;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.order.OrderRequestDto;
import com.tlong.center.api.dto.user.PageResponseDto;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.OrderRepository;
import com.tlong.center.domain.web.WebOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.tlong.center.domain.app.QTlongUser.tlongUser;
import static com.tlong.center.domain.app.goods.QWebGoods.webGoods;
import static com.tlong.center.domain.web.QWebOrder.webOrder;

@Component
@Transactional
public class OrderService {
    final OrderRepository repository;
    final EntityManager entityManager;
    final AppUserRepository appUserRepository;
    JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    public OrderService(EntityManager entityManager, OrderRepository repository,AppUserRepository appUserRepository) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.appUserRepository=appUserRepository;
    }

    /**
     * 分页查询订单列表
     *
     * @param requestDto
     * @return
     */
    public PageResponseDto<OrderRequestDto> findAllOrders(PageAndSortRequestDto requestDto) {
        PageResponseDto<OrderRequestDto> orderRequestDtoPageResponseDto = new PageResponseDto<>();
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);
        Page<WebOrder> orders = repository.findAll(pageRequest);
        List<OrderRequestDto> requestDtos = new ArrayList<>();
        orders.forEach(order -> {
            List<Tuple> tuples = queryFactory.select(tlongUser.realName, tlongUser.phone, tlongUser.userType,webGoods.goodsHead, webGoods.goodsPic,webGoods.publishUserId, webGoods.star,
                    webGoods.goodsCode, webGoods.factoryPrice, webGoods.flagshipPrice, webGoods.founderPrice, webGoods.publishPrice, webGoods.storePrice,
                    webOrder.state, webOrder.placeOrderTime)
                    .from(tlongUser, webGoods, webOrder)
                    .where(tlongUser.id.eq(webOrder.userId)
                            .and(webGoods.id.eq(webOrder.goodsId))
                            .and(tlongUser.id.longValue().eq(order.getUserId()))
                            .and(webGoods.id.longValue().eq(order.getGoodsId())))
                    .fetch();

            tuples.stream().forEach(one -> {
                int userType = one.get(tlongUser.userType);
                OrderRequestDto orderRequestDto = new OrderRequestDto();
                orderRequestDto.setGoodsName(one.get(webGoods.goodsHead));
                orderRequestDto.setUserName(one.get(tlongUser.realName));
                orderRequestDto.setUserType(one.get(tlongUser.userType));
                orderRequestDto.setGoodsUrl(one.get(webGoods.goodsPic).split(",")[0]);
                orderRequestDto.setState(one.get(webOrder.state));
                orderRequestDto.setUserPhone(one.get(tlongUser.phone));
                orderRequestDto.setGoodsCode(one.get(webGoods.goodsCode));
                orderRequestDto.setGoodsStar(one.get(webGoods.star));
                orderRequestDto.setGoodsPrice(userType == 2 ? one.get(webGoods.founderPrice) : userType == 3 ? one.get(webGoods.flagshipPrice) : one.get(webGoods.storePrice));
                orderRequestDto.setPlaceOrderTime(one.get(webOrder.placeOrderTime));
                if (one.get(webGoods.publishUserId)!= null) {
                    TlongUser tlongUser = appUserRepository.findOne(one.get(webGoods.publishUserId));
                    orderRequestDto.setPublishName(tlongUser.getRealName());
                    orderRequestDto.setPublishPhone(tlongUser.getPhone());
                }
                requestDtos.add(orderRequestDto);
            });
        });
        orderRequestDtoPageResponseDto.setList(requestDtos);
        final int[] count = {0};
        Iterable<WebOrder> orders1 = repository.findAll();
        orders1.forEach(order -> {
            count[0]++;
        });
        orderRequestDtoPageResponseDto.setCount(count[0]);
        return orderRequestDtoPageResponseDto;
    }
}

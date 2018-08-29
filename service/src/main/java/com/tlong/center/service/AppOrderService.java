package com.tlong.center.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.tlong.center.api.dto.app.order.AddOrderRequestDto;
import com.tlong.center.api.dto.app.order.OrderOverRequestDto;
import com.tlong.center.api.dto.app.order.OrderOverResponseDto;
import com.tlong.center.api.dto.app.order.OrderSmallDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.dto.quertz.QuartzRequestDto;
import com.tlong.center.api.exception.CustomException;
import com.tlong.center.common.qurtzUtils.QurtzController;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.QTlongUser;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.goods.QWebGoods;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.AppGoodsRepository;
import com.tlong.center.domain.repository.AppUserRepository;
import com.tlong.center.domain.repository.WebOrderRepository;
import com.tlong.center.domain.repository.WebOrgRepository;
import com.tlong.center.domain.web.QWebOrder;
import com.tlong.center.domain.web.QWebOrg;
import com.tlong.center.domain.web.WebOrder;
import com.tlong.center.domain.web.WebOrg;
import org.quartz.SchedulerException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
@Transactional
public class AppOrderService {

    private final WebOrderRepository webOrderRepository;
    private final WebGoodsService webGoodsService;
    private final QurtzController qurtzController;
    private final AppGoodsRepository appGoodsRepository;
    private final AppUserRepository appUserRepository;
    private final WebOrgRepository webOrgRepository;

    public AppOrderService(WebOrderRepository webOrderRepository, WebGoodsService webGoodsService, QurtzController qurtzController, AppGoodsRepository appGoodsRepository, AppUserRepository appUserRepository, WebOrgRepository webOrgRepository) {
        this.webOrderRepository = webOrderRepository;
        this.webGoodsService = webGoodsService;
        this.qurtzController = qurtzController;
        this.appGoodsRepository = appGoodsRepository;
        this.appUserRepository = appUserRepository;
        this.webOrgRepository = webOrgRepository;
    }

    /**
     * 下单
     */
    public TlongResultDto add(AddOrderRequestDto requestDto) {
        webOrderRepository.findAll();
        WebOrder webOrder = new WebOrder();
        webOrder.setGoodsId(requestDto.getGoodsId());
        webOrder.setUserId(requestDto.getUserId());
        //下单人信息
        TlongUser one = appUserRepository.findOne(requestDto.getUserId());
        if (Objects.nonNull(one)){
            webOrder.setUserName(one.getUserName());
            webOrder.setUserCode(one.getUserCode());
        }

        //商品信息
        WebGoods one1 = appGoodsRepository.findOne(requestDto.getGoodsId());
        if (Objects.nonNull(one1)){
            webOrder.setGoodsCode(one1.getGoodsCode());
            webOrder.setGoodsName(one1.getGoodsHead());
            //发布人信息
            TlongUser one2 = appUserRepository.findOne(one1.getPublishUserId());
            if (Objects.nonNull(one2)){
                webOrder.setPublishUserId(one2.getId());
                webOrder.setPublishUserCode(one2.getUserCode());
                webOrder.setPublishUserPhone(one2.getPhone());
            }
        }

        webOrder.setState(1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        webOrder.setPlaceOrderTime(new Date().getTime() /1000 + "");
        webOrder.setCreateTime(format);
        webOrderRepository.save(webOrder);
        //修改商品表中的商品状态
        WebGoods one2 = appGoodsRepository.findOne(requestDto.getGoodsId());
        if (Objects.nonNull(one2)){
            one2.setCurState(2);
            appGoodsRepository.save(one2);
        }
        QuartzRequestDto quartzRequestDto = new QuartzRequestDto();
        quartzRequestDto.setLaterTime(requestDto.getLaterTime());
        quartzRequestDto.setGoodsId(requestDto.getGoodsId());

        /**
         * 创建定时任务
         */
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(() -> {
            try {
                qurtzController.testSchedulerTask(quartzRequestDto);
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        });
        return new TlongResultDto(0,"下单成功！商品已经被锁定，锁定时长：7200M");
    }


    /**
     * 已结缘商品列表
     */
    public Page<OrderSmallDto> orderOverLists(OrderOverRequestDto requestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(requestDto);

        TlongUser tlongUser;
        if (Objects.isNull(requestDto.getUserId())){
//            throw new RuntimeException("用户不存在");
            //用户是游客
            tlongUser = new TlongUser();
            tlongUser.setUserType(null);
            tlongUser.setOrgId(1426L);
        }else {
            tlongUser = userInfo(requestDto.getUserId());
            if (Objects.isNull(tlongUser)){
                throw new RuntimeException("用户不存在");
            }
        }
        List<Long> userIds = new ArrayList<>();
        //判断当前用户是不是管理员
        if (tlongUser.getUserType() == null) {
            //先查出当前管理员所属机构下的所有用户id
            Iterable<WebOrg> all = webOrgRepository.findAll(QWebOrg.webOrg.parentOrgId.eq(tlongUser.getOrgId()));
            List<WebOrg> webOrgs = ToListUtil.IterableToList(all);
            List<Long> orgIds = webOrgs.stream().map(WebOrg::getId).collect(Collectors.toList());
            Iterable<TlongUser> all1 = appUserRepository.findAll(QTlongUser.tlongUser.orgId.in(orgIds));
            List<TlongUser> tlongUsers = ToListUtil.IterableToList(all1);
            userIds = tlongUsers.stream().map(TlongUser::getId).collect(Collectors.toList());
        }else if (tlongUser.getUserType() == 0){
            Iterable<TlongUser> all = appUserRepository.findAll(QTlongUser.tlongUser.parentId.eq(tlongUser.getId()));
            List<TlongUser> tlongUsers = ToListUtil.IterableToList(all);
            userIds = tlongUsers.stream().map(TlongUser::getId).collect(Collectors.toList());
        }else {
            userIds.add(tlongUser.getId());
        }
        BooleanExpression in = QWebOrder.webOrder.userId.in(userIds);
        Page<WebOrder> webOrders = webOrderRepository.findAll(QWebOrder.webOrder.state.eq(requestDto.getState())
                .and(Objects.isNull(requestDto.getUserId()) ? null : in), pageRequest);

//        List<WebOrder> webOrders = ToListUtil.IterableToList(all);
        Page<OrderSmallDto> map = webOrders.map(one -> {
            OrderSmallDto dto = new OrderSmallDto();
            TlongUser one1 = appUserRepository.findOne(one.getUserId());
            if (Objects.nonNull(one1)) {
                //设置真实姓名
                dto.setRealName(one1.getRealName());
                dto.setGoodsId(one.getGoodsId());
                dto.setGoodsCode(one.getGoodsCode());
                dto.setSex(Integer.valueOf(one1.getSex()));

            }
            WebGoods one2 = appGoodsRepository.findOne(one.getGoodsId());
            if (Objects.nonNull(one2)){
                dto.setGoodsPic(one2.getGoodsPic());
                if (one.getPublishUserId() != null) {
                    TlongUser one3 = appUserRepository.findOne(one.getPublishUserId());
                    if (Objects.nonNull(one3)) {
                        //设置成交价格
                        if (Objects.nonNull(one1)) {
                            switch (one3.getLevel()) {
                                case 0:
                                    dto.setTransactionPrice(one2.getFounderPrice());
                                    break;
                                case 1:
                                    dto.setTransactionPrice(one2.getFlagshipPrice());
                                    break;
                            }
                        }

                    }
                }

            }
            dto.setGoodsName(one.getGoodsName());
            dto.setOrderTime(one.getCreateTime());
            return dto;
        });
        return map;
//        OrderOverResponseDto dto = new OrderOverResponseDto();
//        dto.setOrderLists(map);
//        String s = String.valueOf(map.getTotalElements());
//        dto.setCount(Integer.valueOf(s));

//        Page<Long> map1 = webOrders.map(WebOrder::getGoodsId);
//        Iterable<WebGoods> all1 = appGoodsRepository.findAll(QWebGoods.webGoods.id.in(ids));
//        List<WebGoods> webGoods = ToListUtil.IterableToList(all1);
//        Double countflagPrice = 0.0;
//        Double countPublishPrice = 0.0;
//        for (WebGoods webGood : webGoods) {
//            countflagPrice += webGood.getFlagshipPrice();
//            countPublishPrice += webGood.getPublishPrice();
//        }
//        webOrders.forEach(one ->{
//
//        });
//        dto.setCountPublishPrice(countPublishPrice);
//        dto.setCountFlagshipPrice(countflagPrice);
//        return dto;
    }

    public TlongUser userInfo(Long userId){
        TlongUser one1 = appUserRepository.findOne(userId);
        if (Objects.isNull(one1)){
            return null;
        }else {
            return one1;
        }
    }

    /**
     *
     */

}

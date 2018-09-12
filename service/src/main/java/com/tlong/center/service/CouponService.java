package com.tlong.center.service;


import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tlong.center.api.dto.app.activity.AddCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.activity.DeleteCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.coupon.AddCouponRequestDto;
import com.tlong.center.api.dto.app.coupon.CouponEffectResponsDto;
import com.tlong.center.api.dto.app.coupon.CouponResponsDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.api.exception.CustomException;
import com.tlong.center.common.utils.PageAndSortUtil;
import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.TlongUser;
import com.tlong.center.domain.app.coupon.Coupon;
import com.tlong.center.domain.app.coupon.CouponEffect;
import com.tlong.center.domain.app.coupon.UserCoupon;
import com.tlong.center.domain.app.goods.WebGoods;
import com.tlong.center.domain.repository.*;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.coupon.QUserCoupon.userCoupon;
import static com.tlong.center.domain.app.coupon.QCoupon.coupon;
import static com.tlong.center.domain.app.coupon.QCouponEffect.couponEffect1;

@Component
@Transactional
public class CouponService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CouponRepository couponRepository;
    private final CouponEffectRepository couponEffectRepository;
    private final UserCouponRepository userCouponRepository;
    private final TlongUserRepository userRepository;
    private final AppGoodsRepository goodsRepository;
    private final EntityManager entityManager;
    private JPAQueryFactory queryFactory;

    @PostConstruct
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }


    public CouponService(CouponRepository couponRepository, CouponEffectRepository couponEffectRepository, UserCouponRepository userCouponRepository, TlongUserRepository userRepository, AppGoodsRepository goodsRepository, EntityManager entityManager) {
        this.couponRepository = couponRepository;
        this.couponEffectRepository = couponEffectRepository;
        this.userCouponRepository = userCouponRepository;
        this.userRepository = userRepository;
        this.goodsRepository = goodsRepository;
        this.entityManager = entityManager;
    }

    /**
     * 批量生成卡券
     */
    public TlongResultDto addCoupon(AddCouponRequestDto requestDto) {
        Coupon coupon = new Coupon(requestDto);
        coupon.setCurState(0);
        coupon.setNewsTime(new Date().getTime() / 1000);
        coupon.setIsDeleted(0);
        coupon.setRemainNumber(requestDto.getCountNumber());
        couponRepository.save(coupon);
        return new TlongResultDto("本次生成卡券" + requestDto.getCountNumber() + "张");
    }


    /**
     * 发放卡券给个人用户
     */
    public TlongResultDto addCouponToAccount(AddCouponToAccountRequestDto requestDto) {

        //校验用户信息
        checkUserInfo(requestDto.getUserId());

        //获取卡券id 时间戳 map
        Map<Long,Long> couponEndTime = new HashMap<>();
        List<Coupon> all = couponRepository.findAll(requestDto.getCouponIdList());
        all.forEach(one -> couponEndTime.put(one.getId(),one.getEndTime()));

        //削减卡券库存
        updateCouponRemainNumber(requestDto.getCouponIdList());

        //封装存储数据
        List<UserCoupon> collect = requestDto.getCouponIdList().stream().map(one -> {
            UserCoupon userCoupon = new UserCoupon();
            userCoupon.setUserId(requestDto.getUserId());
            userCoupon.setCouponId(one);
            userCoupon.setNewsTime(new Date().getTime() / 1000);
            userCoupon.setEndTime(couponEndTime.get(one));
            userCoupon.setState(0);
            userCoupon.setCurState(0);
            userCoupon.setIsDeleted(0);
            userCoupon.setCouponFlag(UUID.randomUUID().toString());
            return userCoupon;
        }).collect(Collectors.toList());
        userCouponRepository.save(collect);

        return new TlongResultDto(0,"卡券发放完成!");
    }

    //内部方法 削减卡券库存
    private void updateCouponRemainNumber(List<Long> couponIdList) {
        List<Coupon> all = couponRepository.findAll(couponIdList);
        List<Coupon> collect = all.stream().map(one -> {
            if (one.getRemainNumber() >= 1) {
                one.setRemainNumber(one.getRemainNumber() - 1);
                return one;
            } else {
                throw new CustomException("卡券" + one.getCouponName() + "库存不足");
            }
        }).collect(Collectors.toList());
        couponRepository.save(collect);
        logger.info("卡券库存削减完成!");
    }

    //本地方法  校验用户信息
    private void checkUserInfo(Long userId) {
         TlongUser one = userRepository.findOne(userId);
        if (Objects.isNull(one)){
            throw new CustomException("当前用户不存在");
        }
    }

    /**
     * 查询出所有的卡券列表
     */
    public List<CouponResponsDto> findAllCoupon() {
        List<Coupon> all = couponRepository.findAll();
        return all.stream().map(Coupon::toCouponDto).collect(Collectors.toList());
    }

    /**
     * 查询所有的卡券分页列表
     */
    public Page<CouponResponsDto> findAllCouponPage(PageAndSortRequestDto pageAndSortRequestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);
        Page<Coupon> all = couponRepository.findAll(pageRequest);
        return all.map(Coupon::toCouponDto);

    }

    /**
     * 查出一个人所有的优惠券分页列表
     */
    public List<CouponResponsDto> userCouponPage(Long userId) {
        Iterable<UserCoupon> all = userCouponRepository.findAll(userCoupon.userId.eq(userId));
        List<UserCoupon> userCoupons = ToListUtil.IterableToList(all);
        List<Long> collect = userCoupons.stream().map(UserCoupon::getCouponId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(collect)){
            return new ArrayList<>();
        }
        List<Coupon> coupons = couponRepository.findAll(collect);
        if (CollectionUtils.isEmpty(coupons)){
            throw new CustomException("卡券已经不存在!");
        }
        return coupons.stream().map(Coupon::toCouponDto).collect(Collectors.toList());
    }

    /**
     * 根据卡券类型获取效果列表
     */
    public List<CouponEffectResponsDto> couponEffectList(Integer couponType) {
        Iterable<CouponEffect> all = couponEffectRepository.findAll(couponEffect1.couponType.eq(couponType));
        if (all == null){
            return null;
        }
        List<CouponEffect> couponEffects = ToListUtil.IterableToList(all);
        return couponEffects.stream().map(CouponEffect::toDto).collect(Collectors.toList());
    }

    /**
     * 根据卡券类型获取卡券列表
     */
    public Page<CouponResponsDto> couponEffectPage(Integer couponType, PageAndSortRequestDto pageAndSortRequestDto) {
        PageRequest pageRequest = PageAndSortUtil.pageAndSort(pageAndSortRequestDto);
        Page<Coupon> all = couponRepository.findAll(coupon.couponType.eq(couponType)
                .and(coupon.curState.eq(0))
                .and(coupon.isDeleted.eq(0)), pageRequest);
        if (all.getTotalElements() ==0){
            return null;
        }
        return all.map(Coupon::toCouponDto);
    }

    /**
     * 下架某个优惠券
     */
    public TlongResultDto deleteCoupon(Long couponId) {
        Coupon one = couponRepository.findOne(couponId);
        if (Objects.isNull(one)){
            throw new CustomException("优惠券信息不存在!");
        }
        one.setIsDeleted(1);
        couponRepository.save(one);
        return new TlongResultDto(0,"优惠券下架完成!");
    }

    /**
     * 删除某人部分优惠券(消费优惠券)
     */
    public TlongResultDto deleteCouponFromAccount(DeleteCouponToAccountRequestDto requestDto) {
        //验证优惠券有效性
        checkCoupon(requestDto.getCouponIds());

        Iterable<UserCoupon> all = userCouponRepository.findAll(userCoupon.userId.eq(requestDto.getUserId())
                .and(userCoupon.couponId.in(requestDto.getCouponIds())));
        List<UserCoupon> userCoupons = ToListUtil.IterableToList(all);

        if (CollectionUtils.isNotEmpty(userCoupons)){
            userCouponRepository.delete(userCoupons);
            logger.info("优惠券消费成功,本次共消费优惠券" + userCoupons.size() + "张");
        }
        return null;
    }

    //本地方法 验证优惠券有效性
    private void checkCoupon(List<Long> couponIds) {
        Iterable<UserCoupon> all = userCouponRepository.findAll(userCoupon.couponId.in(couponIds)
                .and(userCoupon.curState.ne(0))
                .and(userCoupon.isDeleted.ne(0))
        );
        List<UserCoupon> userCoupons = ToListUtil.IterableToList(all);
        if (CollectionUtils.isNotEmpty(userCoupons)){
            throw new CustomException("部分优惠券已失效");
        }
    }

    /**
     * 根据卡券id获取卡券详细信息
     */
    public CouponResponsDto findCouponById(Long id) {
        Coupon one = couponRepository.findOne(id);
        if (Objects.isNull(one)){
            throw new CustomException("查询的卡券不存在!");
        }
        //获取卡券对应的效果
        CouponEffect one1 = couponEffectRepository.findOne(couponEffect1.id.eq(one.getCouponEffectId()));
        //组装卡券信息对象
        CouponResponsDto responsDto = one.toCouponDto();
        responsDto.setEffectName(one1.getEffectName());
        return responsDto;
    }

    /**
     * 获取商品所有可用优惠券(所有可用的包括未领取的优惠券)
     */
    public List<CouponResponsDto> canUseCouponList(Long goodsId) {
        //获取该商品所属一级分类
        WebGoods one = goodsRepository.findOne(goodsId);
        if (Objects.isNull(one)){
            throw new CustomException("商品信息不存在");
        }
        //获取该商品可用所有优惠券
        List<Coupon> coupons = queryFactory.select(coupon)
                .from(coupon,userCoupon)
//                .innerJoin(userCoupon)
//                .on(coupon.id.eq(userCoupon.couponId)
//                    .and())
                .where(coupon.goodsClass.contains(String.valueOf(one.getGoodsClassId())))
//                    .and(userCoupon.couponId.eq(coupon.id)))
                .distinct()
                .fetch();
        return coupons.stream().map(Coupon::toCouponDto).collect(Collectors.toList());
    }


    /**
     * 获取商品所有可用优惠券(该用户拥有的)
     */
    public List<CouponResponsDto> userCanUseCouponList(Long userId,Long goodsId) {
        //获取该商品所属一级分类
        WebGoods one = goodsRepository.findOne(goodsId);
        if (Objects.isNull(one)){
            throw new CustomException("商品信息不存在");
        }
        //获取该商品可用所有优惠券
        List<Coupon> coupons = queryFactory.select(coupon)
                .from(coupon,userCoupon)
                .where(coupon.goodsClass.contains(String.valueOf(one.getGoodsClassId()))
                    .and(userCoupon.couponId.eq(coupon.id))
                    .and(userCoupon.userId.eq(userId)))
                .distinct()
                .fetch();
        return coupons.stream().map(Coupon::toCouponDto).collect(Collectors.toList());
    }

}

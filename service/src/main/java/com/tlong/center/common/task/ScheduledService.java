package com.tlong.center.common.task;

import com.tlong.center.common.utils.ToListUtil;
import com.tlong.center.domain.app.coupon.Coupon;
import com.tlong.center.domain.app.coupon.UserCoupon;
import com.tlong.center.domain.repository.CouponRepository;
import com.tlong.center.domain.repository.UserCouponRepository;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.tlong.center.domain.app.coupon.QUserCoupon.userCoupon;
import static com.tlong.center.domain.app.coupon.QCoupon.coupon;

@Component
@Transactional
@EnableAsync
public class ScheduledService {

    private final CouponRepository couponRepository;
    private final UserCouponRepository userCouponRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public ScheduledService(CouponRepository couponRepository, UserCouponRepository userCouponRepository) {
        this.couponRepository = couponRepository;
        this.userCouponRepository = userCouponRepository;
    }

    @Scheduled(cron = "59 59 11 * * ?")
    public void clearCoupon() {
        clearnCouponTable();
        clearnUserCouponTable();
        logger.info("今日优惠券,用户优惠券定时任务已经完成!" + new Date());
    }

    private void clearnCouponTable() {
        logger.info("=====>>>>>执行每天午夜的优惠券清除定时任务");
        Iterable<Coupon> all = couponRepository.findAll(coupon.endTime.loe(new Date().getTime() / 1000));

        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (all != null) {
            List<Coupon> coupons = ToListUtil.IterableToList(all);
            List<Coupon> collect = coupons.stream().peek(one -> one.setCurState(1)).collect(Collectors.toList());
            couponRepository.save(collect);
            logger.info("=====>>>>>" + sim.format(new Date()) + "过期卡券已经重新设置过期状态" + "本次共有"
                    + collect.size() + "张卡券过期");
        }
        logger.info("=====>>>>>" + sim.format(new Date()) + "本日无过期优惠券");
    }

    private void clearnUserCouponTable() {
        logger.info("=====>>>>>执行每天午夜的用户优惠券清除定时任务");
        Iterable<UserCoupon> all = userCouponRepository.findAll(userCoupon.endTime.lt(new Date().getTime() / 1000));
        SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (all != null) {
            List<UserCoupon> userCoupons = ToListUtil.IterableToList(all);
            List<UserCoupon> collect = userCoupons.stream().peek(one -> one.setCurState(1)).collect(Collectors.toList());
            userCouponRepository.save(collect);
            logger.info("=====>>>>>" + sim.format(new Date()) + "过期用户卡券已经重新设置过期状态" + "本次共有"
                    + collect.size() + "张卡券过期");
        }
        logger.info("=====>>>>>" + sim.format(new Date()) + "本日无过期用户优惠券");
    }
}


package com.tlong.center.app.coupon;

import com.tlong.center.api.app.coupon.CouponApi;
import com.tlong.center.api.dto.app.activity.AddCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.activity.DeleteCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.coupon.AddCouponRequestDto;
import com.tlong.center.api.dto.app.coupon.CouponEffectResponsDto;
import com.tlong.center.api.dto.app.coupon.CouponResponsDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import com.tlong.center.service.CouponService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/app/coupon")
public class CouponController implements CouponApi {


    private final CouponService couponService;

    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }


    /**
     * 批量生成卡券
     */
    @Override
    public TlongResultDto addCoupon(@RequestBody AddCouponRequestDto requestDto) {
        return couponService.addCoupon(requestDto);
    }

    /**
     * 下架某个优惠券
     */
    @Override
    public TlongResultDto deleteCoupon(@PathVariable Long couponId) {
        return couponService.deleteCoupon(couponId);
    }

    /**
     * 查询出所有的卡券列表
     */
    @Override
    public List<CouponResponsDto> findAllCoupon() {
        return couponService.findAllCoupon();
    }

    /**
     * 查询出所有的卡券分页
     */
    @Override
    public Page<CouponResponsDto> findAllCouponPage(@RequestBody PageAndSortRequestDto pageAndSortRequestDto) {
        return couponService.findAllCouponPage(pageAndSortRequestDto);
    }

    /**
     * 根据卡券类型获取效果列表
     */
    @Override
    public List<CouponEffectResponsDto> couponEffectList(@PathVariable Integer couponType) {
        return couponService.couponEffectList(couponType);
    }

    /**
     * 根据卡券类型获取卡券列表
     */
    @Override
    public Page<CouponResponsDto> couponEffectPage(@PathVariable Integer couponType, @RequestBody PageAndSortRequestDto pageAndSortRequestDto) {
        return couponService.couponEffectPage(couponType,pageAndSortRequestDto);
    }


    /**
     * 根据卡券id获取卡券详细信息
     */
    @Override
    public CouponResponsDto findCouponById(@PathVariable Long id) {
        return couponService.findCouponById(id);
    }

    /**
     * 查出一个人所有的优惠券分页列表
     */
    @Override
    public List<CouponResponsDto> userCouponPage(@PathVariable Long userId) {
        return couponService.userCouponPage(userId);
    }

    /**
     * 发放卡券给个人用户
     */
    @Override
    public TlongResultDto addCouponToAccount(@RequestBody AddCouponToAccountRequestDto requestDto) {
        return couponService.addCouponToAccount(requestDto);
    }

    /**
     * 删除某人部分优惠券(消费优惠券)
     */
    @Override
    public TlongResultDto deleteCouponFromAccount(@RequestBody DeleteCouponToAccountRequestDto requestDto) {
        return couponService.deleteCouponFromAccount(requestDto);
    }

    /**
     * 获取商品所有可用优惠券
     */
    @Override
    public List<CouponResponsDto> canUseCouponList(@PathVariable Long goodsId) {
        return couponService.canUseCouponList(goodsId);
    }

    /**
     * 获取当前商品所有可用优惠券
     */
    @Override
    public List<CouponResponsDto> userCanUseCouponList(@PathVariable Long userId,@PathVariable Long goodsId) {
        return couponService.userCanUseCouponList(userId,goodsId);
    }
}

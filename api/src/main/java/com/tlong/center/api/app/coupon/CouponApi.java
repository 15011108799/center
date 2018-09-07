package com.tlong.center.api.app.coupon;

import com.tlong.center.api.dto.app.activity.AddCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.activity.DeleteCouponToAccountRequestDto;
import com.tlong.center.api.dto.app.coupon.AddCouponRequestDto;
import com.tlong.center.api.dto.app.coupon.CouponEffectResponsDto;
import com.tlong.center.api.dto.app.coupon.CouponResponsDto;
import com.tlong.center.api.dto.common.PageAndSortRequestDto;
import com.tlong.center.api.dto.common.TlongResultDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Api("卡券相关接口")
public interface CouponApi {

    //TODO 卡券消费可用商品 还是商品分类

    @ApiOperation("新增卡券")
    @PostMapping("/addCoupon")  //卡券消费的唯一标示 UUID
    TlongResultDto addCoupon(@RequestBody AddCouponRequestDto requestDto);

    @ApiOperation("下架某个优惠券")
    @PostMapping("/deleteCoupon/{couponId}")
    TlongResultDto deleteCoupon(@PathVariable Long couponId);

    @ApiOperation("获取所有的卡券列表")
    @GetMapping("/couponList")
    List<CouponResponsDto> findAllCoupon();

    @ApiOperation("获取所有的卡券分页列表")
    @GetMapping("/couponPage")
    Page<CouponResponsDto> findAllCouponPage(@RequestBody PageAndSortRequestDto pageAndSortRequestDto);

    @ApiOperation("根据卡券类型获取卡券效果列表")
    @PostMapping("/couponEffectList/{couponType}")
    List<CouponEffectResponsDto> couponEffectList(@PathVariable Integer couponType);

    @ApiOperation("根据卡券类型获取卡券列表")
    @PostMapping("/couponEffectPage/{couponType}")
    Page<CouponResponsDto> couponEffectPage(@PathVariable Integer couponType, @RequestBody PageAndSortRequestDto pageAndSortRequestDto);

    @ApiOperation("根据卡券id获取卡券详细信息")
    @PostMapping("/couponOne/{id}")
    CouponResponsDto findCouponById(@PathVariable Long id);

    @ApiOperation("根据用户id查询出当前用户拥有的所有卡券")
    @PostMapping("/userCouponPage/{userId}")
    List<CouponResponsDto> userCouponPage(@PathVariable Long userId);

    @ApiOperation("添加卡券给个人用户")
    @PostMapping("/addCouponToAccount")
    TlongResultDto addCouponToAccount(@RequestBody AddCouponToAccountRequestDto requestDto);

    @ApiOperation("删除个人用户的卡券(消费)")
    @PostMapping("/deleteCouponFromAccount")
    TlongResultDto deleteCouponFromAccount(@RequestBody DeleteCouponToAccountRequestDto requestDto);


}

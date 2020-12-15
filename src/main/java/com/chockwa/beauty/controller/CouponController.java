package com.chockwa.beauty.controller;

import com.chockwa.beauty.entity.Coupon;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.CouponService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("coupon")
public class CouponController {
    private final CouponService couponService;
    public CouponController(CouponService couponService) {
        this.couponService = couponService;
    }

    @PostMapping("add")
    public Result add(@RequestBody Coupon coupon){
        couponService.add(coupon);
        return Result.SUCCESS();
    }

    @GetMapping("delete")
    public Result delete(Long id){
        couponService.delete(id);
        return Result.SUCCESS();
    }

    @GetMapping("list")
    public Result list(String searchContent){
        return Result.SUCCESS().setData("list", couponService.list(searchContent));
    }
}

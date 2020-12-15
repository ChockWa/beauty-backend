package com.chockwa.beauty.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chockwa.beauty.entity.Coupon;
import com.chockwa.beauty.mapper.CouponMapper;
import com.google.common.base.Strings;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CouponService {
    private final CouponMapper couponMapper;
    public CouponService(CouponMapper couponMapper) {
        this.couponMapper = couponMapper;
    }

    public void add(Coupon coupon){
        if(Strings.isNullOrEmpty(coupon.getContent())){
            throw new IllegalArgumentException("内容不能为空");
        }
        couponMapper.insert(coupon);
    }

    public void delete(long id){
        couponMapper.deleteById(id);
    }

    public List<Coupon> list(String searchContent){
        QueryWrapper<Coupon> wrapper = new QueryWrapper<>();
        wrapper.like(!Strings.isNullOrEmpty(searchContent), "content", searchContent);
        return couponMapper.selectList(wrapper);
    }
}

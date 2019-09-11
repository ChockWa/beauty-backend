package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.ChargeInfo;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/9/11 17:14
 * @description:
 */
@RestController
@RequestMapping("card")
public class CardController extends BaseController{

    @Autowired
    private CardService cardService;

    @RateLimit(fallback = "fallBack")
    @PostMapping("charge")
    public Result charge(@RequestBody ChargeInfo chargeInfo){
        cardService.useCard(chargeInfo.getCardNo(), chargeInfo.getType());
        return Result.SUCCESS();
    }
}

package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.ChargeInfo;
import com.chockwa.beauty.dto.PageParam;
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

    @GetMapping("cards")
    public Result getCardsPage(String cardNo, PageParam pageParam){
        return Result.SUCCESS().setData("cards", cardService.getCardsPage(cardNo, pageParam));
    }

    @GetMapping("add")
    public Result add(String cardNo, Integer type){
        cardService.addCard(cardNo, type);
        return Result.SUCCESS();
    }

    @GetMapping("delete")
    public Result delete(String cardNo){
        cardService.deleteCard(cardNo);
        return Result.SUCCESS();
    }

//    @GetMapping("genCard")
//    public Result genCard(Integer type, Integer count){
//        cardService.genCard(type, count);
//        return Result.SUCCESS();
//    }
}

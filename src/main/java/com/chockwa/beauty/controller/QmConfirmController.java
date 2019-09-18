package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.QmConfirm;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.QmConfirmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/9/17 09:45
 * @description:
 */
@RestController
@RequestMapping("qm-c")
public class QmConfirmController extends BaseController {

    @Autowired
    private QmConfirmService qmConfirmService;

    @RateLimit(fallback = "fallBack")
    @PostMapping("add")
    public Result add(@RequestBody QmConfirm qmConfirm){
        qmConfirmService.add(qmConfirm);
        return Result.SUCCESS();
    }

    @GetMapping("list")
    public Result getListPage(PageParam pageParam, Integer status){
        return Result.SUCCESS().setData("data", qmConfirmService.getListPage(pageParam, status));
    }

    @GetMapping("verify")
    public Result verify(String qmId, Integer status, Integer price){
        qmConfirmService.verify(qmId, status, price);
        return Result.SUCCESS();
    }

    @GetMapping("del")
    public Result delete(String qmId){
        qmConfirmService.delete(qmId);
        return Result.SUCCESS();
    }
}

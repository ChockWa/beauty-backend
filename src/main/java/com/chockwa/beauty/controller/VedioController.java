package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.constant.QmType;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.QmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuohuahe
 * @date: 2019/12/20 11:16
 * @description:
 */
@RestController("/vedio")
public class VedioController extends BaseController{

    @Autowired
    private QmService qmService;

    @RateLimit(fallback = "fallBack")
    @GetMapping("/list")
    public Result list(PageParam pageParam, @RequestParam(required = false)String content){
        return Result.SUCCESS().setData(qmService.selectQmPage(pageParam, null, content, QmType.VEDIO));
    }
}

package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.SnService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuohuahe
 * @date: 2019/10/29 10:19
 * @description:
 */
@RestController
@RequestMapping("sn")
@Slf4j
public class SnController extends BaseController {

    @Autowired
    private SnService snService;

    @RateLimit(fallback = "fallBack")
    @GetMapping("qms")
    public Result qms(PageParam pageParam, @RequestParam(required = false) Integer area,
                      @RequestParam(required = false)String content){
        return Result.SUCCESS().setData("sns", snService.selectSnPage(pageParam, area, content));
    }

    @GetMapping("qmsMgmt")
    public Result qmsMgmt(PageParam pageParam, @RequestParam(required = false) Integer area){
        return Result.SUCCESS().setData("sns", snService.selectSnMgmtPage(pageParam, area));
    }
}

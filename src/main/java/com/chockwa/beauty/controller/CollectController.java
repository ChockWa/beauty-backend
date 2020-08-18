package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.CollectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("collect")
public class CollectController extends BaseController {
    @Autowired
    private CollectService collectService;

    @RateLimit(fallback = "fallBack")
    @GetMapping("list")
    public Result list(PageParam pageParam){
        return Result.SUCCESS().setData(collectService.selectPage(pageParam));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("collect")
    public Result add(String qmId){
        collectService.add(qmId);
        return Result.SUCCESS();
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("remove")
    public Result remove(String qmId){
        collectService.remove(qmId);
        return Result.SUCCESS();
    }
}

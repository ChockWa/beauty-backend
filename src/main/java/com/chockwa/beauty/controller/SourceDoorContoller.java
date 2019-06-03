package com.chockwa.beauty.controller;

import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.SourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/3 18:31
 * @description:
 */
@RestController
@RequestMapping("door")
public class SourceDoorContoller {

    @Autowired
    private SourceService sourceService;

    @GetMapping("newer")
    public Result getNewerSourceList(){
        return Result.SUCCESS().setData("data", sourceService.getNewerSourceList());
    }

    @GetMapping("older")
    public Result getOlderSourceList(){
        return Result.SUCCESS().setData("data", sourceService.getOlderSourceList());
    }

    @GetMapping("hotest")
    public Result getHotestSourceList(){
        return Result.SUCCESS().setData("data", sourceService.getHotestSourceList());
    }
}

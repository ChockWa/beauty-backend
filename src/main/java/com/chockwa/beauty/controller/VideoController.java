package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/video")
public class VideoController extends BaseController {

    @Autowired
    private VideoService videoService;

    @RateLimit(fallback = "fallBack")
    @GetMapping("/videos")
    public Result videos(PageParam pageParam, @RequestParam(required = false)String content){
        return Result.SUCCESS().setData("videos", videoService.selectVideoPage(pageParam, content));
    }

    @GetMapping("/videosMgmt")
    public Result videosMgmt(PageParam pageParam){
        return Result.SUCCESS().setData("videos", videoService.selectVideoMgmtPage(pageParam));
    }

}

package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.common.utils.JwtUtils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.service.CommentService;
import com.chockwa.beauty.service.QmService;
import com.chockwa.beauty.service.VideoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/video")
public class VideoController extends BaseController {

    @Autowired
    private RedisUtils redisUtils;
    @Autowired
    private QmService qmService;
    @Autowired
    private CommentService commentService;
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

    @RateLimit(fallback = "fallBack")
    @GetMapping("/videoInfo")
    public Result getVideoInfo(String qmId, PageParam pageParam, HttpServletRequest request){
        String token = request.getHeader("beautyT");
        if(StringUtils.isBlank(token) || !JwtUtils.verifyToken(token)){
        }else{
            UserInfo.set((User) redisUtils.get(token));
        }
        Map<String, Object> data = new HashMap<>(2);
        data.put("info", qmService.getQmInfo(qmId));
        data.put("comments", commentService.selectCommentPage(qmId, pageParam));
        return Result.SUCCESS().setData("data", data);
    }

}

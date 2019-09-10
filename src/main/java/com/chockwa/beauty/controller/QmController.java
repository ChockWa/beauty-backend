package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.dto.CommentRequest;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.QmInfo;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.CommentService;
import com.chockwa.beauty.service.QmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 15:21
 * @description:
 */
@RestController
@RequestMapping("qm")
public class QmController extends BaseController{

    @Autowired
    private QmService qmService;
    @Autowired
    private CommentService commentService;

    @RateLimit(fallback = "fallBack")
    @GetMapping("qms")
    public Result qms(PageParam pageParam, @RequestParam(required = false) Integer area){
        return Result.SUCCESS().setData("qms", qmService.selectQmPage(pageParam, area));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("info")
    public Result qmInfo(String qmId){
        return Result.SUCCESS().setData("info", qmService.getQmInfo(qmId));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("bQm")
    public Result buyQm(String qmId){
        return Result.SUCCESS().setData("info", qmService.bugQmInfo(qmId));
    }

    @RateLimit(fallback = "fallBack")
    @PostMapping("comment")
    public Result comment(@RequestBody CommentRequest request){
        commentService.qmComment(request.getQmId(), request.getComment());
        return Result.SUCCESS();
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("comments")
    public Result comments(PageParam pageParam, String qmId){
        return Result.SUCCESS().setData("comments", commentService.selectCommentPage(qmId, pageParam));
    }

    @PostMapping("add")
    public Result addQm(@RequestBody QmInfo qmInfo){
        qmService.addQm(qmInfo);
        return Result.SUCCESS();
    }

    @PostMapping("update")
    public Result updateQm(@RequestBody QmInfo qmInfo){
        qmService.updateQm(qmInfo);
        return Result.SUCCESS();
    }

    @GetMapping("delete")
    public Result deleteQm(String qmId){
        qmService.deleteQm(qmId);
        return Result.SUCCESS();
    }
}

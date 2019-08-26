package com.chockwa.beauty.controller;

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
public class QmController {

    @Autowired
    private QmService qmService;
    @Autowired
    private CommentService commentService;

    @GetMapping("qms")
    public Result qms(PageParam pageParam, Integer area){
        return Result.SUCCESS().setData("qms", qmService.selectQmPage(pageParam, area));
    }

    @GetMapping("info")
    public Result qmInfo(String qmId){
        return Result.SUCCESS().setData("info", qmService.getQmInfo(qmId));
    }

    @GetMapping("bQm")
    public Result buyQm(String qmId){
        return Result.SUCCESS().setData("info", qmService.bugQmInfo(qmId));
    }

    @PostMapping("comment")
    public Result comment(@RequestBody CommentRequest request){
        commentService.qmComment(request.getQmId(), request.getComment());
        return Result.SUCCESS();
    }

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

    @DeleteMapping("delete")
    public Result deleteQm(String qmId){
        qmService.deleteQm(qmId);
        return Result.SUCCESS();
    }
}

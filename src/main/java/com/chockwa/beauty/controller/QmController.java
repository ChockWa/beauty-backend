package com.chockwa.beauty.controller;

import com.chockwa.beauty.annotation.RateLimit;
import com.chockwa.beauty.common.utils.JwtUtils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.constant.QmType;
import com.chockwa.beauty.dto.CommentRequest;
import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.dto.PageResult;
import com.chockwa.beauty.entity.QmInfo;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.entity.UserInfo;
import com.chockwa.beauty.exception.BizException;
import com.chockwa.beauty.mapper.UserMapper;
import com.chockwa.beauty.service.CommentService;
import com.chockwa.beauty.service.QmService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/22 15:21
 * @description:
 */
@RestController
@RequestMapping("qm")
@Slf4j
public class QmController extends BaseController{

    @Autowired
    private QmService qmService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private RedisUtils redisUtils;
    @Value("${dns.api-https}")
    private String dnsHttps;

    @RateLimit(fallback = "fallBack")
    @GetMapping("qms")
    public Result qms(PageParam pageParam, @RequestParam(required = false) Integer area,
                      @RequestParam(required = false)String content){
        return Result.SUCCESS().setData("qms", qmService.selectQmPage(pageParam, area, content, QmType.QM.getCode()));
    }

    @GetMapping("qmsMgmt")
    public Result qmsMgmt(PageParam pageParam, @RequestParam(required = false) Integer area){
        return Result.SUCCESS().setData("qms", qmService.selectQmMgmtPage(pageParam, area));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("info")
    public Result qmInfo(String qmId){
        return Result.SUCCESS().setData("info", qmService.getQmInfo(qmId));
    }

    @RateLimit(fallback = "fallBack")
    @GetMapping("info-comment")
    public Result qmInfoWithComment(String qmId, PageParam pageParam){
        Map<String, Object> data = new HashMap<>(2);
//        CompletableFuture future1 = CompletableFuture.supplyAsync(() -> qmService.getQmInfo(qmId));
//        CompletableFuture future2 = CompletableFuture.supplyAsync(() -> commentService.selectCommentPage(qmId, pageParam));
//        CompletableFuture.allOf(future1, future2).join();
//        try {
//
//        } catch (InterruptedException e) {
//            log.error("獲取qm信息和評論線程被打斷", e);
//            Thread.currentThread().interrupt();
//        } catch (ExecutionException e) {
//            log.error("獲取qm信息和評論線程出現異常", e);
//        }
        data.put("info", qmService.getQmInfo(qmId));
        data.put("comments", commentService.selectCommentPage(qmId, pageParam));
        return Result.SUCCESS().setData("data", data);
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

    /**
     * 获取发布器信息
     * @param qmId
     * @return
     */
    @RateLimit(fallback = "fallBack")
    @GetMapping("addrUtilInfo")
    public Result getAddrUtil(String qmId, PageParam pageParam, HttpServletRequest request){
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


    @RateLimit(fallback = "fallBack")
    @GetMapping("qmsQuery")
    public Result qmsQuery(PageParam pageParam, @RequestParam(required = false) Integer area,
                      @RequestParam(required = false)String content, @RequestParam(required = false)Integer type){
        PageResult<QmInfo> pageResult = qmService.selectQmPage(pageParam, area, content, type);
        pageResult.getRecords().forEach(e -> {
            e.setCover(dnsHttps + e.getCover());
        });
        return Result.SUCCESS().setData(pageResult);
    }
}

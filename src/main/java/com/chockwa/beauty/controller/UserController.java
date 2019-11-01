package com.chockwa.beauty.controller;

import com.chockwa.beauty.dto.PageParam;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.MailService;
import com.chockwa.beauty.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuohuahe
 * @date: 2019/2/26 19:47
 * @description:
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @GetMapping("sign")
    public Result sign(){
        userService.sign();
        return Result.SUCCESS();
    }

    @GetMapping("info")
    public Result getUser(){
        return Result.SUCCESS().setData("info", userService.getUser());
    }

    @GetMapping("users")
    public Result getUserListPage(String userName, PageParam pageParam){
        return Result.SUCCESS().setData("data", userService.getUserListPage(userName, pageParam));
    }

    @GetMapping("addCoin")
    public Result addCoin(String uid, Integer coin){
        userService.addCoin(uid, coin);
        return Result.SUCCESS();
    }

    @GetMapping("updateMailNotice")
    public Result sendBeautySiteMessageBatch(){
        mailService.sendBeautySiteMessageBatch();
        return Result.SUCCESS();
    }
}

package com.chockwa.beauty.controller;

import com.chockwa.beauty.entity.Result;
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

    @GetMapping("sign")
    public Result sign(){
        userService.sign();
        return Result.SUCCESS();
    }
}

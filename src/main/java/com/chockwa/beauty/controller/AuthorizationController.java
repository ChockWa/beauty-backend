package com.chockwa.beauty.controller;

import com.chockwa.beauty.dto.LoginDto;
import com.chockwa.beauty.dto.RegisterDto;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 18:02
 * @description:
 */
@RestController
@RequestMapping("auth")
public class AuthorizationController {

    @Autowired
    private AuthorizationService authorizationService;

    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        return Result.SUCCESS().setData("token", authorizationService.login(loginDto.getUserName(), loginDto.getPassword()));
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterDto registerDto){
        return Result.SUCCESS().setData("token", authorizationService.registerAndLogin(registerDto));
    }

}

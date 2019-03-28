package com.chockwa.beauty.controller;

import com.chockwa.beauty.common.utils.VerifyCodeUtils;
import com.chockwa.beauty.dto.LoginDto;
import com.chockwa.beauty.dto.RegisterDto;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @GetMapping("genVerifyCode")
    public void generationVerifyCode(HttpServletResponse response, String uuid){
        try {
            VerifyCodeUtils.outputImage(80,30,response.getOutputStream(),VerifyCodeUtils.generateVerifyCode(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

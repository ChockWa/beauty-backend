package com.chockwa.beauty.controller;

import com.chockwa.beauty.common.utils.MD5Utils;
import com.chockwa.beauty.common.utils.RedisUtils;
import com.chockwa.beauty.common.utils.UUIDUtils;
import com.chockwa.beauty.common.utils.VerifyCodeUtils;
import com.chockwa.beauty.dto.LoginDto;
import com.chockwa.beauty.dto.RegisterDto;
import com.chockwa.beauty.entity.Result;
import com.chockwa.beauty.service.AuthorizationService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AuthorizationController {

    private static final long VERIFYCODE_EXPIRE_SECOND = 1000 * 60 * 5;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private RedisUtils redisUtils;

    @PostMapping("login")
    public Result login(@RequestBody LoginDto loginDto){
        authorizationService.checkVerifyCode(loginDto.getUuid(), loginDto.getVerifyCode());
        return Result.SUCCESS().setData("token", authorizationService.login(loginDto.getUserName(), loginDto.getPassword()))
                .setData("userName", loginDto.getUserName());
    }

    @PostMapping("register")
    public Result register(@RequestBody RegisterDto registerDto){
        authorizationService.checkVerifyCode(registerDto.getUuid(), registerDto.getVerifyCode());
        return Result.SUCCESS().setData("token", authorizationService.registerAndLogin(registerDto))
                .setData("userName", registerDto.getUserName());
    }

    @GetMapping("genVerifyCode")
    public void generationVerifyCode(HttpServletResponse response, String uuid){
        try {
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);
            VerifyCodeUtils.outputImage(80,30,response.getOutputStream(),verifyCode);
            redisUtils.set(uuid, verifyCode, VERIFYCODE_EXPIRE_SECOND);
        } catch (IOException e) {
            log.error("Failed to get verification code", e);
        }
    }

    public static void main(String[] args) {
        String salt = UUIDUtils.getUuid();
        System.out.println(salt);
        System.out.println(MD5Utils.md5(salt + "123456" + salt));
    }
}

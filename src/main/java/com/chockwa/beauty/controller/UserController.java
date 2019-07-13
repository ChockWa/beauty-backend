package com.chockwa.beauty.controller;

import cn.hutool.core.io.FileUtil;
import com.chockwa.beauty.entity.User;
import com.chockwa.beauty.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @auther: zhuohuahe
 * @date: 2019/2/26 19:47
 * @description:
 */
@RestController
public class UserController {

    @Autowired
    private UserMapper userMapper;

    @GetMapping("/getUser")
    public User getUser(){
        return userMapper.selectById(1L);
    }

}

package com.chockwa.beauty.controller;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/18 18:25
 * @description:
 */
public class BaseController {

    public void fallBack(){
        throw new RuntimeException("Server is busy!Please try again!");
    }
}

package com.chockwa.beauty.controller;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/18 18:25
 * @description:
 */
public class BaseController {

    public void fallBack(){
        throw new RuntimeException("服務器繁忙，請稍後重試！");
    }
}

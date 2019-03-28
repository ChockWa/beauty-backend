package com.chockwa.beauty.entity;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/28 17:20
 * @description:
 */
public class UserInfo {

    public static ThreadLocal<User> userInfo = new ThreadLocal<>();

    public static void set(User user){
        userInfo.set(user);
    }

    public static User get(){
        return userInfo.get();
    }
}

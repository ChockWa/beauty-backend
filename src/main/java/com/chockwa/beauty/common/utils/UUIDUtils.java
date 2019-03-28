package com.chockwa.beauty.common.utils;

import java.util.UUID;

/**
 * @auther: zhuohuahe
 * @date: 2019/3/5 14:39
 * @description:
 */
public class UUIDUtils {

    public static String getUuid(){
        return UUID.randomUUID().toString().replace("-","");
    }

}

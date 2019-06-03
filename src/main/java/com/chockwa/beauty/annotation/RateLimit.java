package com.chockwa.beauty.annotation;

import java.lang.annotation.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/3 10:50
 * @description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    String fallback() default "";
}

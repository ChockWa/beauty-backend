package com.chockwa.beauty.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/3 10:48
 * @description:
 */
@Component
public class RateLimiter {

    private static final int SECOND_MAX_CONCURRENT = 100;

    private static final AtomicInteger COUNT = new AtomicInteger();

    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);

    public void check(Object controller, String fallBackMethodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(controller == null || StringUtils.isBlank(fallBackMethodName)){
            return;
        }
        if(COUNT.decrementAndGet() <= 0){
            Method fallBackMethod = controller.getClass().getMethod(fallBackMethodName, null);
            fallBackMethod.invoke(controller, null);
        }
    }

    @PostConstruct
    public void supplement(){
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                COUNT.set(SECOND_MAX_CONCURRENT);
            }
        }, 500, 10000, TimeUnit.MILLISECONDS);
    }
}

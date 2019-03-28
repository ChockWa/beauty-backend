package com.chockwa.beauty.common.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisUtils {

    @Autowired
    private RedisTemplate redisTemplate;

    public boolean setIfNotExist(String key, Object value) {
        return redisTemplate.opsForValue().setIfAbsent(key, value);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long milliSecond) {
        redisTemplate.opsForValue().set(key, value, milliSecond, TimeUnit.MILLISECONDS);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Object getAndSet(String key, Object value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    public Set<String> keys(String keyPrefix) {
        return redisTemplate.keys(keyPrefix);
    }

    public long inc(String key) {
        return incBy(key, 1);
    }

    public long incBy(String key, int step) {
        return redisTemplate.opsForValue().increment(key, step);
    }

    public void deleteByKeys(List<String> keys) {
        redisTemplate.delete(keys);
    }

    public void deleteByKey(String key) {
        redisTemplate.delete(key);
    }
}

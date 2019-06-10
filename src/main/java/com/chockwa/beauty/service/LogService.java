package com.chockwa.beauty.service;

import com.chockwa.beauty.entity.Log;
import com.chockwa.beauty.mapper.LogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/10 17:12
 * @description:
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    public void add(Log log){
        logMapper.insert(log);
    }
}

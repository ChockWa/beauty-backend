package com.chockwa.beauty.disruptor;

import com.alibaba.fastjson.JSON;
import com.lmax.disruptor.WorkHandler;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:49
 * @description: 日志事件处理器
 */
public class LogEventHandler implements WorkHandler<LogEvent> {
    @Override
    public void onEvent(LogEvent logEvent) throws Exception {
        System.out.println(JSON.toJSON(logEvent));
    }
}

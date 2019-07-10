package com.chockwa.beauty.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:24
 * @description: 日志事件工厂
 */
public class LogEventFactory implements EventFactory<LogEvent> {
    @Override
    public LogEvent newInstance() {
        return new LogEvent();
    }
}

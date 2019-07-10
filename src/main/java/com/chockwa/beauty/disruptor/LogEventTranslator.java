package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.entity.Log;
import com.lmax.disruptor.EventTranslatorOneArg;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:29
 * @description: 日志事件转换器
 */
public class LogEventTranslator implements EventTranslatorOneArg<LogEvent, Log> {
    @Override
    public void translateTo(LogEvent logEvent, long l, Log log) {
        logEvent.setLog(log);
    }
}

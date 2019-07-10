package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.entity.Log;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import lombok.AllArgsConstructor;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:42
 * @description:
 */
@AllArgsConstructor
public class LogEventProducer {

    private EventTranslatorOneArg translator;

    private RingBuffer<LogEvent> ringBuffer;

    public void recordLog(Log log){
        this.ringBuffer.publishEvent(translator, log);
    }
}

package com.chockwa.beauty.disruptor;

import com.lmax.disruptor.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/2 16:18
 * @description:
 */
@Slf4j
public class LogEventExceptionHandler implements ExceptionHandler {

    @Override
    public void handleEventException(Throwable throwable, long sequence, Object event) {
        log.error("log event disruptor process data error sequence ==[{}] event==[{}] ,ex ==[{}]", sequence, event.toString(), throwable.getMessage());
    }

    @Override
    public void handleOnStartException(Throwable throwable) {
        log.error("start disruptor error ==[{}]!", throwable.getMessage());
    }

    @Override
    public void handleOnShutdownException(Throwable throwable) {
        log.error("shutdown disruptor error ==[{}]!", throwable.getMessage());
    }
}

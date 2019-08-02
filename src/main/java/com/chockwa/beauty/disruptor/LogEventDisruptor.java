package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.service.LogService;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 18:24
 * @description:
 */
@Component
@Slf4j
public class LogEventDisruptor {

    private static final int bufferSize = 1024;

    private Disruptor<LogEvent> disruptor;

    private RingBuffer<LogEvent> ringBuffer;

    @Autowired
    private LogService logService;

    @PostConstruct
    public synchronized void init(){
        if(disruptor == null){
            disruptor = new Disruptor<>(new LogEventFactory(), bufferSize, new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "LogEventDisruptor-Thread");
                }
            });
            disruptor.handleEventsWithWorkerPool(new LogEventHandler(logService));
            disruptor.setDefaultExceptionHandler(new LogEventExceptionHandler());
            ringBuffer = disruptor.start();
        }
    }

    public RingBuffer<LogEvent> getRingBuffer(){
        return ringBuffer;
    }

    @PreDestroy
    public void destory(){
        try {
            disruptor.shutdown(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            log.error("LogEventDisruptor close time out", e);
        }
    }
}

package com.chockwa.beauty.disruptor;

import com.lmax.disruptor.TimeoutException;
import com.lmax.disruptor.dsl.Disruptor;
import lombok.extern.slf4j.Slf4j;
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

    @PostConstruct
    public void init(){
        disruptor = new Disruptor<>(new LogEventFactory(), bufferSize, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "LogEventDisruptor-Thread");
            }
        });
        disruptor.handleEventsWithWorkerPool(new LogEventHandler());
        disruptor.start();
    }

    public Disruptor<LogEvent> get(){
        return disruptor;
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

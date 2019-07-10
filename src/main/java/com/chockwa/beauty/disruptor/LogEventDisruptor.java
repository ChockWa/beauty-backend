package com.chockwa.beauty.disruptor;

import com.lmax.disruptor.dsl.Disruptor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.ThreadFactory;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 18:24
 * @description:
 */
@Component
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
}

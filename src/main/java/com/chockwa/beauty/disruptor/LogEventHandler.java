package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.service.LogService;
import com.lmax.disruptor.WorkHandler;
import lombok.AllArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:49
 * @description: 日志事件处理器
 */
@Setter
@AllArgsConstructor
public class LogEventHandler implements WorkHandler<LogEvent> {

    private LogService logService;

    @Override
    public void onEvent(LogEvent logEvent) throws Exception {
        logService.add(logEvent.getLog());
    }
}

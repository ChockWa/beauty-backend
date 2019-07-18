package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.common.utils.SpringUtil;
import com.chockwa.beauty.service.LogService;
import com.lmax.disruptor.WorkHandler;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:49
 * @description: 日志事件处理器
 */
public class LogEventHandler implements WorkHandler<LogEvent> {

//    private LogService logService = SpringUtil.getBean(LogService.class);

    @Override
    public void onEvent(LogEvent logEvent) throws Exception {
//        logService.add(logEvent.getLog());
    }
}

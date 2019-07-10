package com.chockwa.beauty.disruptor;

import com.chockwa.beauty.entity.Log;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @auther: zhuohuahe
 * @date: 2019/7/10 17:23
 * @description: 日志事件实体
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LogEvent {
    private Log log;
}

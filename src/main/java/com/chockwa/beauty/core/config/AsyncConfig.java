package com.chockwa.beauty.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @auther: zhuohuahe
 * @date: 2019/6/5 17:24
 * @description:
 */
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean
    @Primary
    public TaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setMaxPoolSize(5);
        taskExecutor.setCorePoolSize(3);
        taskExecutor.setQueueCapacity(10);
        taskExecutor.setThreadNamePrefix("zip_generator_thread");
        taskExecutor.initialize();
        return taskExecutor;
    }
}

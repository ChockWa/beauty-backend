package com.chockwa.beauty.core.util;

import java.util.concurrent.*;

/**
 * @auther: zhuohuahe
 * @date: 2019/8/9 11:04
 * @description:
 */
public class TaskExecutor extends ThreadPoolExecutor {

    private static final String THREAD_GROUP_NAME = "task-executor-group";

    private static final int POOL_SIZE = 2 * Runtime.getRuntime().availableProcessors() + 1;

    private static volatile TaskExecutor executor;

    public static TaskExecutor getExecutor(){
        if(executor == null){
            synchronized(TaskExecutor.class){
                if(executor == null){
                    executor = new TaskExecutor();
                }
            }
        }
        return executor;
    }

    private TaskExecutor() {
        super(POOL_SIZE, POOL_SIZE, 2L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread t = new Thread(new ThreadGroup(THREAD_GROUP_NAME), r, "thread");
                if (t.isDaemon())
                    t.setDaemon(false);
                if (t.getPriority() != Thread.NORM_PRIORITY)
                    t.setPriority(Thread.NORM_PRIORITY);
                return t;
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {
        TaskExecutor executor = getExecutor();
        executor.execute(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("awake...");
        });
        System.out.println("main thread");
    }
}

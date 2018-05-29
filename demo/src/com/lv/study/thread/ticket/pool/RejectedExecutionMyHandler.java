package com.lv.study.thread.ticket.pool;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lvbaolin on 2018/5/7.
 *
 * 饱和策略
 *
 */
public class RejectedExecutionMyHandler  implements RejectedExecutionHandler {

    private static ExecutorService singleThreadPool = null;

    static {
        singleThreadPool = Executors.newSingleThreadExecutor();
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor poolExecutor) {
        System.out.println("------线程超过最大线程数，建议修改最大线程数，自定义处理机制------");
//        System.out.println("当前线程池活跃数：{}"+poolExecutor.getActiveCount());
//        System.out.println("当前线程池排队数：{}"+ poolExecutor.getQueue().size());
//        System.out.println("当前线程池大小：{}"+ poolExecutor.getPoolSize());
//        System.out.println("最大线程数：{}"+poolExecutor.getLargestPoolSize());
//        System.out.println("任务数：{}"+poolExecutor.getTaskCount());
        singleThreadPool.execute(r);
        singleThreadPool.shutdown();
    }
}

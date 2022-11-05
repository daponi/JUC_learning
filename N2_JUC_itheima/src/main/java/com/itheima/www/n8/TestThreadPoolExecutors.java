package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * newCachedThreadPool
 *
 * 核心线程数 == 最大线程数（没有救急线程被创建），因此也无需超时时间
 * 阻塞队列是无界的，可以放任意数量的任务
 * 评价 适用于任务量已知，不是太繁忙，相对耗时的任务
 */
@Slf4j
public class TestThreadPoolExecutors {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(3);

        // 自定义线程池的线程名
/*        ExecutorService pool = Executors.newFixedThreadPool(3, new ThreadFactory() {
            AtomicInteger num = new AtomicInteger(1);
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"myPool"+num.getAndIncrement());
            }
        });*/


        pool.execute(() -> log.debug("111"));
        pool.execute(() -> log.debug("222"));
        pool.execute(() -> log.debug("333"));
        pool.execute(() -> log.debug("444"));

    }
}

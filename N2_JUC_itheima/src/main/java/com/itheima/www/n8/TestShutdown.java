package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 线程池状态变为 SHUTDOWN
 * - 不会接收新任务
 * - 但已提交任务会执行完
 * - 此方法不会阻塞调用线程的执行，即t1调用该方法后线程继续执行下去，不会等
 *
 * awaitTermination 要用在 shutdown 之后，不然没有 shutdown 的标记，会一直等待下去
 */
@Slf4j
public class TestShutdown {
    public static void main(String[] args) throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<String> future = pool.submit(() -> {
                    log.debug("task 1 running...");
                    Thread.sleep(3000);
                    log.debug("task 1 finish ...");
                    return "1";
                }
        );

        Future<String> future2 = pool.submit(() -> {
                    log.debug("task 2 running...");
                    Thread.sleep(1000);
                    log.debug("task 2 finish ...");
                    return "1";
                }
        );

        // 任务进入队列
        Future<String> future3 = pool.submit(() -> {
                    log.debug("task 3 running...");
                    Thread.sleep(1000);
                    log.debug("task 3 finish ...");
                    return "1";
                }
        );

        log.debug("shutdown...");
        pool.shutdown();
        log.debug("end .........");
        boolean b = pool.awaitTermination(2, TimeUnit.SECONDS);
        log.debug("awaitTermination....{}",b);

    }
}

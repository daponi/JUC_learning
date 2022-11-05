package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.*;

/**
 * 线程池状态变为 STOP
 * - 不会接收新任务
 * - 会将队列中的任务作为结果返回
 * - 并用 interrupt 的方式中断正在执行的任务
 */
@Slf4j
public class TestShutdownNow {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        Future<String> future = pool.submit(() -> {
                    log.debug("task 1 running...");
                    Thread.sleep(1000);
                    log.debug("task 1 finish ...");
                    return "1";
                }
        );

        Future<String> future2 = pool.submit(() -> {
                    log.debug("task 2running...");
                    Thread.sleep(1000);
                    log.debug("task 2 finish ...");
                    return "1";
                }
        );

        Future<String> future3 = pool.submit(() -> {
                    log.debug("task 3 running...");
                    Thread.sleep(1000);
                    log.debug("task 3 finish ...");
                    return "1";
                }
        );

        log.debug("shutdown...");
        List<Runnable> runnables = pool.shutdownNow();

        log.debug("返回 :{}",runnables);
        log.debug("end .........");
        boolean b = pool.awaitTermination(2, TimeUnit.SECONDS);
        log.debug("awaitTermination....{}",b);


    }
}

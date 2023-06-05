package com.bilibili.juc.cf;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CompletableFuture的创建方式：
 * 1. 构造函数创建，用的少；
 * 2. runAsync()方法不支持返回值。其中Executor指的是可以传入我们的线程池对象；
 * 3. .supplyAsync()可以支持返回值。其中Executor指的是可以传入我们的线程池对象；
 */

@Slf4j
public class CompletableFutureDemo02 {
    public static void main(String[] args) throws Exception {
        // constructCompletableFuture();
        runAsync();
        // supplyAsync();
    }

    public static void constructCompletableFuture() {
        CompletableFuture<String> cf = new CompletableFuture<>();

        new Thread(() -> {
            log.info("子线程开始");
            try {
                Thread.sleep(3000);
                //2. 此时，如果在另外一个线程中，主动设置该CompletableFuture的值，则上面线程中的结果就能返回。
                cf.complete("result111");
                log.info("is ending");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        log.info("开始join阻塞");
        //1. CompletableFuture还没有任何计算结果，这时调用join，当前线程会一直阻塞在这里。
        String result = cf.join();
        log.info("join结束");
        log.info("{}", result);

    }

    public static void runAsync() {
        log.info(":主线程开始");
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        //运行一个没有返回值的异步任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            log.info(":子线程启动干活");
            log.info("是否为守护线程: " + Thread.currentThread().isDaemon());
            try {
                Thread.sleep(5000);
                log.info(":is ending");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // },executorService);//若使用自定义线程池创建则不是守护线程

        log.info("{}",future.join());
        //主线程阻塞future.get();
        log.info(":主线程结束");
        executorService.shutdown();
    }

    public static void supplyAsync() throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(3);

        log.info(":主线程开始");

        //运行一个有返回值的异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            log.info(":子线程启动干活");
            log.info("是否为守护线程: " + Thread.currentThread().isDaemon());
            try {
                Thread.sleep(5000);
                log.info(":is ending");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "子线程完成了!";
        });
        // },executorService);//若使用自定义线程池创建则不是守护线程

        //主线程阻塞
        String s = future.get();
        log.info("主线程结束, 子线程的结果为:" + s);
        executorService.shutdown();

    }
}

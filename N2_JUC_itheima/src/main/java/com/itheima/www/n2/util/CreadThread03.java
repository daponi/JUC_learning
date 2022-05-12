package com.itheima.www.n2.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * 03.通过Callable接口创建线程
 */

@Slf4j
public class CreadThread03 {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> ft = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                log.info("running...");
                Thread.sleep(2000);
                return 2000;
            }
        });

        Thread t = new Thread(ft,"Thread01");
        t.start();
        log.info("{}",ft.get());
    }



}

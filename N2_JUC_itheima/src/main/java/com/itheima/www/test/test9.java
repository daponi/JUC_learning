package com.itheima.www.test;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * yieID()的使用
 */
@Slf4j
public class test9 {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                int k=1;
                log.info("k={}",k++);
                log.info("k={}",k++);
                Thread.sleep(1000);
                Thread.yield();
                log.info("k={}",k++);
                log.info("k={}",k++);
            }
        };
        Thread t2 = new Thread(){
            @SneakyThrows
            @Override
            public void run() {
                log.info("t2 start...");
                Thread.sleep(1000);
                log.info("t2 end...");

            }
        };

        t1.start();
        t2.start();
    }
}

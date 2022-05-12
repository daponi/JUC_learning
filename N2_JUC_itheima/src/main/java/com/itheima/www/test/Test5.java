package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 查看线程的状态
 */
@Slf4j(topic = "c.Test5")
public class Test5 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        System.out.println(t1.getState());
        t1.start();
        System.out.println(t1.getState());
        t1.join();
        System.out.println(t1.getState());
    }
}

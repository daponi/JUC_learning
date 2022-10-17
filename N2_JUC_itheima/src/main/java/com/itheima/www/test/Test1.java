package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建线程的方法
 */
@Slf4j(topic = "c.Test1")
@SuppressWarnings("all")
public class Test1 {
    public static void main(String[] args) {
        test1();
        test2();
    }
    public static void test2() {
        Runnable run=new Runnable() {
            @Override
            public void run() {
            log.debug("running");
            }
        };

        // Thread t = new Thread(run ,"t2");
        Thread t = new Thread(() -> {
            log.debug("running");
        }, "t2");

        t.start();
    }

    public static void test1() {
        Thread t = new Thread() {
            @Override
            public void run() {
                log.debug("running");
            }
        };
        t.setName("t1");
        t.start();

    }
}

package com.itheima.www.n2;

import lombok.extern.slf4j.Slf4j;

/**
 * 01.通过Thread类 或继承Thread类 创建线程
 */
@Slf4j(topic = "c.Thread01")
// public class CreadThread01 extends Thread{
public class CreadThread01 {
    public static void main(String[] args) {
        Thread thread01 = new Thread("Thread01") {
            @Override
            public void run() {
                log.info("Thread--running...");
            }
        };

        // 用lambda实现
        Thread thread02 = new Thread(() -> log.info("Thread--running..."));

        // thread01.setName("Thread01");
        /**
         * start方法是让线程进入初始化阶段，需要等到cpu的时间片轮转给了它，才是真正的进入运行状态
         */
        thread01.start();
        log.info("main--running");
    }

}

package com.itheima.www.n2;

import lombok.extern.slf4j.Slf4j;

/**
 * 02.通过实现Runnable接口 创建线程
 */
@Slf4j(topic = "c.Thread02")
public class CreadThread02 {
    public static void main(String[] args) {
        Runnable runnable01 = new Runnable() {
            @Override
            public void run() {
                log.info("running...");
            }
        };

        // 用lambda实现
        Runnable runnable02 = () -> log.info("running...");
        Thread thread01 = new Thread(runnable01,"Thread01");
        // thread01.setName("Thread01");
        /**
         * start方法是让线程进入初始化阶段，需要等到cpu的时间片轮转给了它，才是真正的进入运行状态
         */
        thread01.start();
        log.info("main--running");
    }

}

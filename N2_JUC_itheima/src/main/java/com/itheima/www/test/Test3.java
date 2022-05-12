package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试死循环的java进程
 */
@Slf4j(topic = "c.Test3")
public class Test3 {
    public static void main(String[] args) {
        new Thread(() -> {
            while(true) {
                log.debug("running...");
            }
        }, "t1").start();

        new Thread(() -> {
            while(true) {
                log.debug("running...");
            }
        }, "t2").start();
    }
}

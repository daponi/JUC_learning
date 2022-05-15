package com.itheima.www.test;


import lombok.extern.slf4j.Slf4j;

/**
 * 守护线程、用户线程
 * 默认情况下，Java 进程需要等待所有线程都运行结束，才会结束。
 * 有一种特殊的线程叫做守护线程，只要其它非守护线程都运行结束了，即使守护线程的代码没有执行完，也会强制结束。
 */
@Slf4j(topic = "c.Test15")
public class Test15 {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    break;
                }
            }
            log.debug("结束");
        }, "t1");
        t1.setDaemon(true);// 设置为守护线程
        t1.start();

        Thread.sleep(1000);
        log.debug("结束");
    }
}


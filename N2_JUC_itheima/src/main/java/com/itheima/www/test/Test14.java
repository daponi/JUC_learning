package com.itheima.www.test;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * LockSupport.park()的使用：
 * LockSupport.park();用于禁止当前线程进行线程调度，除非许可证可用。
 * 使用interrupt()打断该线程时，将打断标记设为真即isInterrupted()重新设置为true，线程会从park()位置继续执行；
 * 倘若打断标记为假即isInterrupted()重新设置为false，则park()仍会暂停线程。
 */
@Slf4j(topic = "c.Test14")
public class Test14 {
    public static void main(String[] args) throws InterruptedException {
        test1(); //线程只暂停一次，被打断后就会直接走完
        // test2();
    }


    private static void test1() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();// 此时正常打断，打断标记设为true
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted()); //true
            LockSupport.park(); // 由于打断标记为true，此时线程不会在该行代码暂停
        }, "t1");
        t1.start();

        sleep(1);
        t1.interrupt();

    }

    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("park...");
            LockSupport.park();// 此时正常打断，打断标记设为true
            log.debug("unpark...");
            log.debug("打断状态：{}", Thread.interrupted());// interrupted()返回打断标记后会清除打断标记，将其设为false
            log.debug("打断状态：{}", Thread.currentThread().isInterrupted()); // 可知此时打断标记为false
            LockSupport.park(); // 由于打断标记为false，此时线程仍会在该行代码暂停
        }, "t1");
        t1.start();

        sleep(1);
        t1.interrupt();

    }
    private static void test3() {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                log.debug("park...");
                LockSupport.park();// 此时正常打断，打断标记设为true
                log.debug("打断状态：{}", Thread.interrupted());// interrupted()判断后会清除打断标记，将其设为false
                LockSupport.park(); // 由于打断标记为false，此时线程会在该行代码暂停

            }
        });
        t1.start();

        sleep(1);
        t1.interrupt();
    }

}
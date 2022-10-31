package com.itheima.www.n4.deadlock;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 死锁问题
 * 操作系统死锁的四大条件：互斥，请求与保持，不可剥夺，循环等待
 *
 * 除了活锁外，死锁和线程饥饿都可以用ReentrantLock的锁超时来解决
 */
@Slf4j
public class TestDeadLock {
    public static void main(String[] args) {
        test1();
    }

    private static void test1() {
        Object a = new Object();
        Object b = new Object();

        Thread t1 = new Thread(() -> {
            synchronized(a){

            log.debug("lock A...");
            Sleeper.sleep(1);
            synchronized (b) {
                log.debug("lock B...");
                log.debug("操作》。。。");
            }
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            synchronized(b){

            log.debug("lock A...");
            Sleeper.sleep(1);
            synchronized (a) {
                log.debug("lock B...");
                log.debug("操作》。。。");
            }
            }
        }, "t2");
        t1.start();
        t2.start();
    }
}


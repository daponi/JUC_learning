package com.itheima.www.test;

import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步模式之顺序控制
 * 比如，两个线程必须先 2 后 1 打印
 * 1.使用synochronized实现
 * 2.使用park/unpark实现
 * 3. 使用reentrantLock实现
 */
@Slf4j
public class Test26 {
    static ReentrantLock reentrantLock = new ReentrantLock();
    static boolean has2Runn = false;

    public static void main(String[] args) {
        parkMethod();
        reentrantLockMethod();
    }

    /**
     * 使用park/unpark实现
     */
    public static void parkMethod() {
        Thread t1 = new Thread(() -> {
            // 当没有『许可』时，当前线程暂停运行；有『许可』时，用掉这个『许可』，当前线程恢复运行
            LockSupport.park();
            log.debug("1");
        }, "t1");
        t1.start();

        new Thread(() -> {
            log.debug("2");
            // 给线程 t1 发放『许可』（多次连续调用 unpark 只会发放一个『许可』
            LockSupport.unpark(t1);
        }, "t2").start();
    }

    public static void reentrantLockMethod() {
        Condition waitOne = reentrantLock.newCondition();

        new Thread(() -> {
            reentrantLock.lock();
            try {
                log.debug("2");
                has2Runn = true;
                waitOne.signal();
            } finally {
                reentrantLock.unlock();
            }
        }, "t4").start();

        new Thread(() -> {
            reentrantLock.lock();

            try {
                while (!has2Runn) {
                    try {
                        waitOne.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            } finally {
                reentrantLock.unlock();
            }
        }, "t3").start();


    }
}

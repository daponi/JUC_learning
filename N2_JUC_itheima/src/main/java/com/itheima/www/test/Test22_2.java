package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock相对于 synchronized 它具备如下特点
 * 1.可中断
 * 2.可以设置超时时间
 * 3.可以设置为公平锁
 * 4.支持多个条件变量
 * 与 synchronized 一样，都支持可重入
 *
 * 可中断 ：当前线程进入entryList等待锁进入阻塞状态时，可以被其它线程打
 * 注意如果是不可中断模式，那么即使使用了interrupt也不会让等待中断
 */
@Slf4j
public class Test22_2 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            try {
                log.debug("开始获取锁。。。");
                lock.lockInterruptibly(); // 当前线程进入entryList等待锁进入阻塞状态时，可以被其它线程打断
            } catch (InterruptedException e) {
                log.debug("等待锁的过程被打断。。。");
                e.printStackTrace();
                return; // 不执行下面的代码
            }
            try {
                log.debug("成功获取了锁》。。");
            } finally {
                lock.unlock();
            }
        }, "t1");

        lock.lock(); //main线程先加锁
        log.debug("main获得了锁。。。");
        t1.start(); // t1线程进入阻塞
        try {
            TimeUnit.SECONDS.sleep(1);
            log.debug("开始intterrupt》。。");
            t1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            log.debug("main线程释放锁。。。");
            lock.unlock();
        }
    }
}

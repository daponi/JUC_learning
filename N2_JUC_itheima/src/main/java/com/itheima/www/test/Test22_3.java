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
 * <p>
 * 超时时间，尝试获取锁ReentrantLock.tryLock
 */
@Slf4j
public class Test22_3 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        new Thread(() -> {
            log.debug("{}启动....",Thread.currentThread().getName());
            try {
                // if (!lock.tryLock()) { // 不设置时限
                if (!lock.tryLock(3,TimeUnit.SECONDS)) { // 尝试获取锁2秒
                    log.debug("获取锁失败，立即返回");
                    return;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                log.debug("获取锁成功...");
            } finally {
                lock.unlock();
            }
        }, "t1").start();

        lock.lock();
        try {
            log.debug("主线程先加锁");
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            log.debug("主线程释放了锁。。。");
            lock.unlock();
        }
    }
}

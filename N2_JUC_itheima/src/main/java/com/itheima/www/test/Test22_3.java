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
 * 锁超时：尝试获取锁ReentrantLock.tryLock若成功获取锁或者锁已经被当前线程持有，返回true，否则返回false且不会进入阻塞状态。
 * tryLock(long timeout, TimeUnit unit)  可带参数进行超时获取锁，时间到了线程被唤醒，且时间内可被其它线程interrupt()
 */
@Slf4j
public class Test22_3 {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        Thread t1=new Thread(() -> {
            log.debug("{}启动....",Thread.currentThread().getName());
            try {
                // if (!lock.tryLock()) { // 不设置时限
                if (!lock.tryLock(3,TimeUnit.SECONDS)) { // 尝试获取锁2秒
                    log.debug("获取锁失败，立即返回");
                    return;
                }
            } catch (InterruptedException e) {
                log.debug("线程被打断");
                e.printStackTrace();
                throw new RuntimeException("线程被打断了");
            }
            try {
                log.debug("获取锁成功...");
            } finally {
                lock.unlock();
            }
        }, "t1");t1.start();

        lock.lock();
        try {
            log.debug("主线程先加锁");
            TimeUnit.SECONDS.sleep(2);
            t1.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();

        } finally {
            log.debug("主线程释放了锁。。。");
            lock.unlock();

        }
    }
}

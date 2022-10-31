package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 其它干活的线程，都要一直阻塞，效率太低
 * 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来，
 * 加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加 synchronized 就好像 main 线程是翻窗户进来的
 * 解决方法，使用 wait - notify 机制
 * 使用while保持自选加锁，方便notifyAll唤醒符合目标的线程
 * <p>
 * 改进为ReentrantLock机制，使用其多个条件变量进行进程间的通信
 */
@Slf4j(topic = "c.TestCorrectPosture")
public class Test24 {
    static final Object room = new Object();
    static boolean hasCigarette = false;
    static boolean hasTakeout = false;
    static ReentrantLock reentrant = new ReentrantLock();
    // 等香烟的休息室waitSet
    static Condition waitCigarete = reentrant.newCondition();
    //等外卖的休息室waitSet
    static Condition waitTakeout = reentrant.newCondition();


    public static void main(String[] args) throws InterruptedException {

        new Thread(() -> {
            reentrant.lock();
            try {
                log.debug("有烟没？[{}]", hasCigarette);
                while (!hasCigarette) { // 使用while代替if，成自旋锁,若notifyAll后该线程仍不满足条件则继续wait下去
                    log.debug("没烟，先歇会！");
                    try {
                        waitCigarete.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟了，可以开始干活了");
            } finally {
                reentrant.unlock();
            }
        }, "小南").start();

        new Thread(() -> {
            reentrant.lock();
            try {
                log.debug("外卖送到没？[{}]", hasTakeout);
                while (!hasTakeout) {
                    log.debug("没外卖，先歇会！");
                    try {
                        waitTakeout.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("外卖到了，可以开始干活了");
            } finally {
                reentrant.unlock();
            }
        }, "小女").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            reentrant.lock();
            try {
                hasTakeout = true;
                log.debug("外卖到了噢！");
                waitTakeout.signal(); // 唤醒线程
            } finally {
                reentrant.unlock();
            }

        }, "送外卖的").start();

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            reentrant.lock();
            try {
                hasCigarette = true;
                log.debug("香烟到了噢！");
                waitCigarete.signal();// 唤醒线程
            } finally {
                reentrant.unlock();
            }

        }, "送香烟的").start();


    }

}

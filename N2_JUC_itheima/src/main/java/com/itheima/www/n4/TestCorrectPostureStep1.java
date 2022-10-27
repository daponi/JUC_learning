package com.itheima.www.n4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * sleep(long n)和wait(long n)的区别
 * 1) sleep 是 Thread 方法，而 wait 是 Object 的方法
 * 2) sleep 不需要强制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用
 * 3) sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁
 * 4) 它们状态 TIMED_WAITING，它们都会释放CPU且都能被interrupted方法打断，sleep自动结束被唤醒，而wait是进入waitSet其它等待线程被唤醒
 *
 * 其它干活的线程，都要一直阻塞，效率太低
 * 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来，
 * 加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加 synchronized 就好像 main 线程是翻窗户进来的
 * 解决方法，使用 wait - notify 机制
 */
@Slf4j
public class TestCorrectPostureStep1 {
    static final Object room = new Object();
    static boolean hasCigarette = false;//有没有烟

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {

                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇息会!");
                    try {
                        TimeUnit.SECONDS.sleep(2); //sleep不会释放锁，则其它线程不能获得锁只能干等着
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("有烟没？[{}]", hasCigarette);
                if (hasCigarette) {
                    log.debug("开始干活...");
                }
            }
        }, "小南").start();

        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                synchronized (room) {
                    log.debug("开始干活...");
                }
            },"其他人").start();
        }

        TimeUnit.SECONDS.sleep(1);
        new Thread(() -> {
            // 这里能不能加 synchronized (room)？没加 synchronized 就好像 main 线程是翻窗户进来的
            synchronized (room) {
                hasCigarette = true;
                log.debug("烟到了噢！");
            }
        }, "送烟的").start();

    }
}

package com.itheima.www.n4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 其它干活的线程，都要一直阻塞，效率太低
 * 小南线程必须睡足 2s 后才能醒来，就算烟提前送到，也无法立刻醒来，
 * 加了 synchronized (room) 后，就好比小南在里面反锁了门睡觉，烟根本没法送进门，main 没加 synchronized 就好像 main 线程是翻窗户进来的
 *
 * 解决方法，使用 wait - notify 机制
 * 问题: 当waitSet里被wait的线程不止小南一个时，notify是随机唤醒waitSet里一个线程则小南线程还是有可能没被唤醒
 */
@Slf4j
public class TestCorrectPostureStep2 {
    static final Object room = new Object();
    static boolean hasCigarette = false;//有没有烟
    static boolean hasTakeOut = false;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            synchronized (room) {

                log.debug("有烟没？[{}]", hasCigarette);
                if (!hasCigarette) {
                    log.debug("没烟，先歇息会!");
                    try {
                        // TimeUnit.SECONDS.sleep(2); //sleep不会释放锁，则其它线程不能获得锁只能干等着
                        room.wait();
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
                room.notify();
            }
        }, "送烟的").start();

    }
}

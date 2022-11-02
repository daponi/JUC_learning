package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicInteger;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * 源码之 LongAdder
 * LongAdder 是并发大师 @author Doug Lea （大哥李）的作品，设计的非常精巧
 *
 * transient volatile Cell[] cells; // 累加单元数组, 懒惰初始化
 * transient volatile long base;    // 基础值, 如果没有竞争, 则用 cas 累加这个域
 * transient volatile int cellsBusy; // 在 cells 创建或扩容时, 置为 1, 表示加锁
 *
 *
 * 使用的是CAS锁，实际开发中不能用这种锁，但它底层里是这么写的
 */
@Slf4j(topic = "c.Test42")
public class LockCas {
    // 0 没加锁
    // 1 加锁
    private AtomicInteger state = new AtomicInteger(0);

    public void lock() {
        while (true) {
            if (state.compareAndSet(0, 1)) {
                break;
            }
        }
    }

    public void unlock() {
        log.debug("unlock...");
        state.set(0);
    }

    public static void main(String[] args) {
        LockCas lock = new LockCas();
        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
                sleep(1); //睡眠阻塞时，其它线程访问lock则一直while里空运转，类似于锁，称为CAS锁
            } finally {
                lock.unlock();
            }
        }).start();

        new Thread(() -> {
            log.debug("begin...");
            lock.lock();
            try {
                log.debug("lock...");
            } finally {
                lock.unlock();
            }
        }).start();
    }
}

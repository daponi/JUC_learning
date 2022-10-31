package com.itheima.www.n4;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;


/**
 * 活锁：活锁出现在两个线程互相改变对方的结束条件，最后谁也无法结束
 * 活锁是线程在跑，但是由于共享资源的状态一直在被其他线程往结束状态相反的方向改变，导致线程一直跑，但跑不完
 * 如Redis的缓存雪崩
 */
@Slf4j(topic = "c.TestLiveLock")
public class TestLiveLock {
    static volatile int count = 10;
    static final Object lock = new Object();

    public static void main(String[] args) {
        new Thread(() -> {
            // 期望减到 0 退出循环
            while (count > 0) {
                Sleeper.sleep(0.2);
                count--;
                log.debug("count: {}", count);
            }
        }, "t1").start();
        new Thread(() -> {
            // 期望超过 20 退出循环
            while (count < 20) {
                Sleeper.sleep(0.2);
                count++;
                log.debug("count: {}", count);
            }
        }, "t2").start();
    }
}

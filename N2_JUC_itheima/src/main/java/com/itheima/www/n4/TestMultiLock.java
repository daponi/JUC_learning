package com.itheima.www.n4;


import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

/**
 * 多把不相干的锁
 * 将锁的粒度细分
 *  好处，是可以增强并发度
 *  坏处，如果一个线程需要同时获得多把锁，就容易发生死锁
 */
public class TestMultiLock {
    public static void main(String[] args) {
        BigRoom bigRoom = new BigRoom();
        new Thread(() -> {
            bigRoom.study();
        },"小南").start();
        new Thread(() -> {
            bigRoom.sleep();
        },"小女").start();
    }
}

@Slf4j(topic = "c.BigRoom")
class BigRoom {
    // 将锁细分
    private final Object studyRoom = new Object();

    private final Object bedRoom = new Object();

    public void sleep() {
        synchronized (bedRoom) { // synchronized(this)
            log.debug("sleeping 2 小时");
            Sleeper.sleep(2);
        }
    }

    public void study() {
        synchronized (studyRoom) { // synchronized(this)
            log.debug("study 1 小时");
            Sleeper.sleep(1);
        }
    }

}

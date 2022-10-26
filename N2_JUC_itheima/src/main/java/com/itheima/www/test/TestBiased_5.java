package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;
import java.util.concurrent.locks.LockSupport;

/**
 * 先加 VM 参数 -XX:BiasedLockingStartupDelay=0 来禁用延迟偏向锁
 * 当JVM连续撤销同一个类对象偏向锁阈值超过 40 次后，jvm 会这样觉得，自己确实偏向错了，
 * 根本就不该偏向。于是整个类的所有对象都会变为不可偏向的，新建的该类对象也是不可偏向的
 * t1线程对39个Horse类的对象为t1线程id的偏向锁101，
 * t2线程对前19个Horse类对象偏向t1锁升级为轻量锁000再撤销偏向性001，后20个类对象为t2线程id的偏向锁101
 * t3线程对前19个Horse类对象从无偏向锁001升级为轻量锁000再撤销偏向性为001
 * 对后20个Horse类对象从偏向锁t2锁升级为轻量锁000再撤销偏向性001
 * main再创建Horse对象时是无偏向001
 */
@Slf4j
public class TestBiased_5 {
    static Thread t1, t2, t3;

    public static void main(String[] args) throws InterruptedException {
        Vector<Horse> list = new Vector<>();
        int loopNumber = 39;
        // t1线程对39个对象加锁，让其为t1线程id的偏向锁101
        t1 = new Thread(() -> {
            for (int i = 0; i < loopNumber; i++) {
                Horse horse = new Horse();
                list.add(horse);
                synchronized (horse) {
                    log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
                }
            }
            LockSupport.unpark(t2);
        }, "t1");
        t1.start();
        t2 = new Thread(() -> {
            LockSupport.park();
            log.debug("===============> ");
            for (int i = 0; i < loopNumber; i++) {
                Horse horse = list.get(i);
                log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
                synchronized (horse) {
                    log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
                }
                log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
            }
            LockSupport.unpark(t3);
        }, "t2");
        t2.start();
        t3 = new Thread(() -> {
            LockSupport.park();
            log.debug("===============> ");
            for (int i = 0; i < loopNumber; i++) {
                Horse horse = list.get(i);
                log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
                synchronized (horse) {
                    log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
                }
                log.debug(i + "\t" + ClassLayout.parseInstance(horse).toPrintableSimple());
            }
        }, "t3");
        t3.start();
        t3.join();
        log.debug(ClassLayout.parseInstance(new Horse()).toPrintableSimple());
    }
}

class Horse {
}
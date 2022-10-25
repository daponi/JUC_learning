package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.concurrent.TimeUnit;

/**
 * 先加 VM 参数 -XX:BiasedLockingStartupDelay=0 来禁用延迟偏向锁
 * 撤销偏向性： 1.使用hashCode
 * 调用了对象的 hashCode，但偏向锁的对象 MarkWord 中存储的是线程 id，如果调用 hashCode 会导致偏向锁被
 * 轻量级锁会在锁记录中记录 hashCode，重量级锁会在 Monitor 中记录 hashCode
 * 2.其它线程使用对象，锁会升级为轻量锁，使用完毕会撤销偏向性
 * 3.调用 wait/notify，因为wait/notify是重量锁使用的
 */
@Slf4j
public class TestBiased_3 {
    public static void main(String[] args) {
        Fish fish = new Fish();
        System.out.println(TimeUnit.HOURS);
        Thread t1 = new Thread(() -> {
            log.debug(ClassLayout.parseInstance(fish).toPrintableSimple());
            synchronized (fish) {
                log.debug(ClassLayout.parseInstance(fish).toPrintableSimple());

                try {
                    fish.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "t1");
        t1.start();

        new Thread(() -> {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (fish) {

                log.debug("notify:{}", ClassLayout.parseInstance(fish).toPrintableSimple());
                fish.notify();
            }
        }, "t2").start();

    }
}

class Fish {
}
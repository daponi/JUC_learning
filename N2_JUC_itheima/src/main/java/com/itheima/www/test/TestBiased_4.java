package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

import java.util.Vector;

/**
 * 先加 VM 参数 -XX:BiasedLockingStartupDelay=0 来禁用延迟偏向锁
 * 批量重偏向:
 * 当对象虽然被多个线程访问，但没有竞争，这时偏向了线程 T1 的对象仍有机会重新偏向 T2，重偏向会重置对象的 Thread ID;
 * 当撤销偏向锁阈值超过 20 次后，jvm 会这样觉得，我是不是偏向错了呢，于是会在给这些对象加锁时重新偏向至加锁线程
 *
 * 1）【线程1】1-30次加锁都是101，即偏向锁状态；这个没什么问题，线程1首次加的锁，并且没有别的线程竞争，所以对象头是偏向锁状态，对应的Thread Id为线程1.
 * 2）【线程2】1-19加的锁都是轻量级锁，即前19次进行了偏向锁撤销，第20次及以后执行了重偏向，线程id指向线程2；
 */
@Slf4j
public class TestBiased_4 {
    public static void main(String[] args) {
        Vector<Bird> list = new Vector<>();//线程安全的list
        new Thread(() -> {
            for (int i = 0; i < 30; i++) {
                Bird bird = new Bird();
                list.add(bird);
                synchronized (bird) {
                    log.debug(i + "\t" + ClassLayout.parseInstance(bird).toPrintableSimple());
                }
            }
            synchronized (list) { //保证t1线程先加锁再到t2线程加锁
                list.notify();
            }

// 如果不用 wait/notify 使用 join 必须打开下面的注释
// 因为：t1 线程不能结束，否则底层线程可能被 jvm 重用作为 t2 线程，底层线程 id 是一样的
/*try {
System.in.read();
} catch (IOException e) {
e.printStackTrace();
}*/


        }, "t1").start();

        new Thread(() -> {
            synchronized (list) {
                try {
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            for (int i = 0; i < 30; i++) {
                Bird bird = list.get(i);
                log.debug(i + "锁前" + "\t" + ClassLayout.parseInstance(bird).toPrintableSimple());
                synchronized (bird) {
                    log.debug(i + "锁中" + "\t" + ClassLayout.parseInstance(bird).toPrintableSimple());
                }
                log.debug(i + "锁后" + "\t" + ClassLayout.parseInstance(bird).toPrintableSimple());
            }
        }, "t2").start();

    }
}

class Bird {
}

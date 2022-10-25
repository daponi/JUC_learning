package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 撤销偏向性： 1.使用hashCode
 * 调用了对象的 hashCode，但偏向锁的对象 MarkWord 中存储的是线程 id，如果调用 hashCode 会导致偏向锁被
 * 轻量级锁会在锁记录中记录 hashCode，重量级锁会在 Monitor 中记录 hashCode
 *  2.其它线程使用对象，锁会升级为轻量锁，使用完毕会撤销偏向性
 *  3.调用 wait/notify，因为wait/notify是重量锁使用的
 *
 *
 * 先加 VM 参数 -XX:BiasedLockingStartupDelay=0 来禁用延迟偏向锁
 * 有其它线程使用已被线程加锁为偏向锁101的对象时，其它线程会将偏向锁升级为轻量级锁00,轻量级锁使用完毕会撤销其锁对象的偏向性，变为正常的001
 */
@Slf4j
public class TestBiased_2 {
    public static void main(String[] args) throws InterruptedException {
        Cat cat = new Cat();
        new Thread(() -> {
            log.debug("t1,前：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//101,默认应该是偏向锁101，但偏向锁是默认是延迟的，不会在程序启动时立即生效所以是正常不带锁001
            synchronized (cat) {
                log.debug("t1,中：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//101，t1线程向dog对象施加偏向锁101
            }
            log.debug("t1,后：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//101，偏向锁101，且有线程ID

            synchronized (TestBiased_2.class) { //保证t1执行完再执行t2线程
                TestBiased_2.class.notify();
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
            synchronized (TestBiased_2.class) {
                try {
                    TestBiased_2.class.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("t2,前：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//101
            synchronized (cat) {
                log.debug("t2,中：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//升级为轻量锁thin lock:00，偏向锁对象有t1的ID，此时线程t2使用时会将偏向锁升级位轻量锁00
            }
            log.debug("t2,后：{}",ClassLayout.parseInstance(cat).toPrintableSimple());//轻量锁使用会被撤销偏向性，变成00
            log.debug("========================================");

        }, "t2").start();

    }
}

class Cat {

}

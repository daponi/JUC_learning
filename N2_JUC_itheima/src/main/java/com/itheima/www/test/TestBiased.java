package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;
import org.openjdk.jol.info.ClassLayout;

/**
 * 测试对象偏向锁的信息
 * 一个对象创建时：
 * 如果开启了偏向锁（默认开启），那么对象创建后，markword 值正常16位为 0x05 即最后 3 位为 101，这时它的
 * thread、epoch、age 都为 0
 * 偏向锁是默认是延迟的，不会在程序启动时立即生效，如果想避免延迟，可以加 VM 参数 - * XX:BiasedLockingStartupDelay=0 来禁用延迟偏向锁
 * 如果没有开启偏向锁，那么对象创建后，markword 值为 0x01 即最后 3 位为 001，这时它的 hashcode、 * age 都为 0，第一次用到 hashcode 时才会赋值
 *
 *
 * 撤销偏向性： 1.使用hashCode
 * 调用了对象的 hashCode，但偏向锁的对象 MarkWord 中存储的是线程 id，如果调用 hashCode 会导致偏向锁被
 * 轻量级锁会在锁记录中记录 hashCode
 * 重量级锁会在 Monitor 中记录 hashCode
 * 2.其它线程使用对象，锁会升级为轻量锁，使用完毕会撤销偏向性
 * 3.调用 wait/notify，因为wait/notify是重量锁使用的
 */
@Slf4j
public class TestBiased {
    public static void main(String[] args) throws InterruptedException {
        Dog dog = new Dog();
        Dog dog3 = new Dog();
        dog3.hashCode(); // 生成hashCode后会取消掉偏向锁，因为偏向锁Mark Word存放的是线程ID
        // log.debug(ClassLayout.parseInstance(dog).toPrintable());
        log.debug(ClassLayout.parseInstance(dog).toPrintableSimple());// 默认是开启偏向锁的，
        Thread.sleep(4000);//若不加VM参数则用睡眠来抵消延迟
        // log.debug(ClassLayout.parseInstance(new Dog()).toPrintable());
        // log.debug(ClassLayout.parseInstance(new Dog()).toPrintable());
        log.debug(ClassLayout.parseInstance(new Dog()).toPrintableSimple());
        log.debug(ClassLayout.parseInstance(dog3).toPrintableSimple());


        new Thread(() -> {
        Dog dog2 = new Dog();
            log.debug("synchronized 前");
            System.out.println(ClassLayout.parseInstance(dog2).toPrintableSimple());
            synchronized (dog2) {
                log.debug("synchronized 中");
                System.out.println(ClassLayout.parseInstance(dog2).toPrintableSimple());
            }
            log.debug("synchronized 后");
            System.out.println(ClassLayout.parseInstance(dog2).toPrintableSimple());
        }, "t1").start();


    }


}

class Dog{

}

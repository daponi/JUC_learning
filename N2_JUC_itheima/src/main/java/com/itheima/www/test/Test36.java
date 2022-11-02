package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 6.4 原子引用
 * 为什么需要原子引用类型？
 * AtomicReference   : 引用类型的原子操作
 * AtomicMarkableReference : 可以判断版本号的引用类型原子操作
 * AtomicStampedReference
 *
 * 需求：只要有其它线程【动过了】共享变量，那么自己的 cas 就算失败，这时，仅比较值是不够的，需要再加一个版本号
 */
@Slf4j
public class Test36 {
    static AtomicStampedReference<String> ref = new AtomicStampedReference<>("A", 1);

    public static void main(String[] args) throws InterruptedException {
        String prev = ref.getReference();
        int stamp = ref.getStamp();
        TimeUnit.SECONDS.sleep(1);
        log.debug("reference:{} ,stamp:{}", prev, stamp);
        change();
        TimeUnit.SECONDS.sleep(1);
        log.debug("change A->B {}",ref.compareAndSet(prev, "B", stamp, stamp+1));
        log.debug("reference:{} ,stamp:{}", ref.getReference(), ref.getStamp());
    }

    public static void change() {
        Thread t1 = new Thread(() -> {
            String prev = ref.getReference();
            int stamp = ref.getStamp();
            log.debug("reference:{} ,stamp:{}", prev, stamp);

            log.debug("change A->B {}",ref.compareAndSet(prev, "B", stamp, stamp += 1));
        });

        Thread t2 = new Thread(() -> {
            try {
                t1.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String prev = ref.getReference();
            int stamp = ref.getStamp();
            log.debug("reference:{} ,stamp:{}", prev, stamp);
            log.debug("change B->A {}",ref.compareAndSet(prev, "A", stamp, stamp += 1));
        });
        t1.start();
        t2.start();

    }
}

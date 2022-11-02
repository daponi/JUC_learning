package com.itheima.www.test;

import com.sun.org.apache.bcel.internal.generic.NEW;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;

/**
 * /**
 *  * 6.4 原子引用
 *  * 为什么需要原子引用类型？
 *  * AtomicReference   : 引用类型的原子操作
 *  * AtomicMarkableReference : 可以判断版本号的引用类型原子操作
 *  * AtomicStampedReference
 *  *
 *  * 需求：有时候，并不关心引用变量更改了几次，只是单纯的关心是否更改过，所以就有了 * AtomicMarkableReference
 *  * 垃圾袋满了就换，且只换一次，true垃圾满，false没满
 *  */
@Slf4j
public class Test38 {
    public static void main(String[] args) throws InterruptedException {
        GarbageBag bag = new GarbageBag("垃圾袋装满了垃圾。。。");
        AtomicMarkableReference<GarbageBag> ref = new AtomicMarkableReference<>(bag, true);
        log.debug("start....");
        GarbageBag prev = ref.getReference();
        log.debug("垃圾袋: {}",prev);

        new Thread(()->{
        log.debug("start....");
        bag.setDesc("垃圾被倒完的空垃圾袋");
        ref.compareAndSet(bag, bag, true, false);
        log.debug(bag.toString());
        }).start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("需要换一只新垃圾袋？");
        boolean success = ref.compareAndSet(prev, new GarbageBag("空垃圾袋"), true, false);
        log.debug("换了么？" + success);
        log.debug(ref.getReference().toString());
    }
}

//垃圾袋
class  GarbageBag{
    String desc;

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public GarbageBag(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        // 输出地址+desc，看是否为同一个对象
        return super.toString() + " " + desc;
    }
}

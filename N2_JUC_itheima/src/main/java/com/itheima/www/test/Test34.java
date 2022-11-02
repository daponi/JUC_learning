package com.itheima.www.test;

import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntUnaryOperator;

/**
 * 原子整数 AtomicInteger
 */
public class Test34 {
    public static void main(String[] args) {
        AtomicInteger i = new AtomicInteger(2);
        System.out.println(i.incrementAndGet());//先自增1再返回  3
        System.out.println(i.getAndIncrement());//先返回再自增1  3
        System.out.println(i.decrementAndGet());//先自减1再返回  3
        System.out.println(i.getAndDecrement());//先返回再自减   3

        System.out.println(i.getAndAdd(5));//先+5再返回   2
        System.out.println(i.addAndGet(-1 * 5));//乘以-5  2

        System.out.println(i.updateAndGet(value -> -1 * value));// -2，源码就是CAS

        System.out.println(updateAndGet(i, Value -> Value * -5)); //10
    }

    // 根据源码自定义updateAndGet
    public static int updateAndGet(AtomicInteger i, IntUnaryOperator operator){
        while (true) {
            int prev=i.get();
            int next=operator.applyAsInt(prev);
            if (i.compareAndSet(prev, next)) {
                return next;
            }
        }
    }
}

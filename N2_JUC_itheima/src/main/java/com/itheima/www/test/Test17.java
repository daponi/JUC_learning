package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 临界区的读写问题，内存可见性
 * 一段代码块内如果存在对共享资源的进行多线程读写操作，称这段代码块为临界区
 * 多个线程在临界区内执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了竞态条件
 *
 * 1.使用synchronized进行互斥来保住临界区的正常运行
 * 思考:
 * synchronized 实际是用对象锁保证了临界区内代码的原子性，临界区内的代码对外是不可分割的，不会被线程切换所打断。
 * 为了加深理解，请思考下面的问题
 * 如果把 synchronized(obj) 放在 for 循环的外面，如何理解？-- 原子性 ,即把该for循环视为一个原子操作
 * 如果 t1 synchronized(obj1) 而 t2 synchronized(obj2) 会怎样运作？-- 锁对象，不是同一把锁不能互斥
 * 如果 t1 synchronized(obj) 而 t2 没有加会怎么样？如何理解？-- 锁对象
 */
@Slf4j(topic = "c.Test17")
public class Test17 {
    static int counter = 0;
    static final Object room = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room) {
                    counter++;
                }
            }
        }, "t1");
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                synchronized (room) {
                    counter--;
                }
            }
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        log.debug("{}", counter);
    }
}
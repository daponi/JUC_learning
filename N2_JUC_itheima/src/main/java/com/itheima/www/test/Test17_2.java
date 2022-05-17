package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 临界区的读写问题，内存可见性
 * 一段代码块内如果存在对共享资源的进行多线程读写操作，称这段代码块为临界区
 * 多个线程在临界区内执行，由于代码的执行序列不同而导致结果无法预测，称之为发生了竞态条件
 *
 * 面向对象改进
 * 把需要保护的共享变量放入一个类
 */
@Slf4j(topic = "c.Test17_2")
public class Test17_2 {
    public static void main(String[] args) throws InterruptedException {
        Room room = new Room();
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.increment();
            }
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 5000; i++) {
                room.decrement();
            }
        }, "t2");

        t1.start();
        t2.start();
        t1.join();
        // log.debug("{}", room.getCounter()); //此处的getCounter()需要加synchronized，因为操作没完，会上下文切换读取的不一定是最新值
        t2.join();
        log.debug("{}", room.getCounter()); //此处的getCounter()可不加synchronized，因为上面的操作都做完了
    }
}

class Room {
    private int counter = 0;

    public synchronized void increment() {
        counter++;
    }

    public void decrement() {
        synchronized(this){
            counter--;
        }
    }

    public synchronized int getCounter() {
        return counter;
    }
}

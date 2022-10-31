package com.itheima.www.test;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 同步模式之顺序控制--交替输出：
 *      1.wait/notify 版
 *      2.ReentrantLock版
 *      3.park/unpark版
 * 超级经典的并发面试题：
 * 线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现？
 * 分析：要运用面向对象的思想以满足扩展性,使用ReentrantLock给每个线程固定的Condition，进行精确唤醒和输出
 * 用三个线程使用同一个打印工具类对象来加锁打印,关键在于如何构建这个工具类？
 * 设三个标识位: 输出内容    当前Condition     下一个Condition
 * a           a             a
 * b           b             b
 * c           c             c
 */

public class Test30 {
    public static void main(String[] args) throws InterruptedException {
        AwaitSignal awaitSignal = new AwaitSignal(5);
        Condition a = awaitSignal.newCondition();
        Condition b = awaitSignal.newCondition();
        Condition c = awaitSignal.newCondition();

        new Thread(() -> {
            awaitSignal.print("a", a, b);
        }).start();

        new Thread(() -> {
            awaitSignal.print("b", b, c);
        }).start();

        new Thread(() -> {
            awaitSignal.print("c", c, a);
        }).start();

        TimeUnit.SECONDS.sleep(1);
        awaitSignal.lock();
        try {
            System.out.println("开始打印》...");
            // 唤醒a开始打印
            a.signal();
        } finally {
            awaitSignal.unlock();
        }
    }
}

class AwaitSignal extends ReentrantLock {
    // 循环次数
    private int loopNumber;

    public AwaitSignal(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    /**
     * 目标Condition的线程打印
     * 参数1 打印内容， 参数2 进入哪一间休息室, 参数3 下一间休息室
     * 线程第一次进入该方法是将自己线程放入队友waitSet阻塞，signal唤醒后才开始打印
     * @param str     打印Str
     * @param current 当前Condition
     * @param next    下一个唤醒的Condition
     */
    public void print(String str, Condition current, Condition next) {
        for (int i = 0; i < loopNumber; i++) {
            lock();
            try {
                current.await(); // 第一次将线程先放进对应的waitSet内
                System.out.printf(str);
                next.signal();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                unlock();
            }
        }
    }
}
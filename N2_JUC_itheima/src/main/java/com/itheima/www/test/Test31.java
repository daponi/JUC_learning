package com.itheima.www.test;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 同步模式之顺序控制--交替输出：
 * 1.wait/notify 版
 * 2.ReentrantLock版
 * 3.park/unpark版
 * 超级经典的并发面试题：
 * 线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现？
 * 分析：要运用面向对象的思想以满足扩展性,使用ReentrantLock给每个线程固定的Condition，进行精确唤醒和输出
 * 用三个线程使用同一个打印工具类对象来加锁打印,关键在于如何构建这个工具类？
 * 设两个标识位: 输出内容   下一个线程
 * a             b
 * b             c
 * c             a
 */
public class Test31 {
    static Thread t1;
    static Thread t2;
    static Thread t3;
    public static void main(String[] args) throws InterruptedException {
        ParkUnpark parkUnpark = new ParkUnpark(5);
        t1=new Thread(()->{
            parkUnpark.print("a", t2);
        });
        t2=new Thread(()->{
            parkUnpark.print("b", t3);
        });
        t3=new Thread(()->{
            parkUnpark.print("c", t1);
        });
        t1.start();
        t2.start();
        t3.start();

        TimeUnit.SECONDS.sleep(1);
        LockSupport.unpark(t1);
    }
}

class ParkUnpark {
    // 循环次数
    private int loopNumber;

    public ParkUnpark(int loopNumber) {
        this.loopNumber = loopNumber;
    }

    public void print(String str, Thread next) {
        for (int i = 0; i < loopNumber; i++) {
            LockSupport.park();// 第一次将线程先放进waitSet内
            System.out.printf(str);
            LockSupport.unpark(next);
        }
    }
}

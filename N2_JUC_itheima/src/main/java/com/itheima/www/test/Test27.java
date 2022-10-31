package com.itheima.www.test;

/**
 * 同步模式之顺序控制--交替输出：
 *      1.wait/notify 版；
 *      2.ReentrantLock版
 *      3.park/unpark版
 * 超级经典的并发面试题：
 * 线程 1 输出 a 5 次，线程 2 输出 b 5 次，线程 3 输出 c 5 次。现在要求输出 abcabcabcabcabc 怎么实现？
 * 分析：不能用boolean作为标识位，因为它只有2个状态，要运用面向对象的思想以满足扩展性
 * 用三个线程使用同一个打印工具类对象来加锁打印,关键在于如何构建这个工具类？
 * 设三个标识位: 输出内容       等待标记     下一个标记
 * a           1             2
 * b           2             3
 * c           3             1
 */

public class Test27 {
    public static void main(String[] args) {
        WaitNotify waitNotify = new WaitNotify(1, 5);
        new Thread(()->{
            waitNotify.print("a", 1, 2);
        },"t1").start();

        new Thread(()->{
            waitNotify.print("b", 2, 3);
        },"t2").start();

        new Thread(()->{
            waitNotify.print("c", 3, 1);
        },"t3").start();
    }
}

// 打印工具类
class WaitNotify {
    // 线程对应的等待标记 ,1,2,3，开始第一位输出的标记
    private int flag;
    // 循环次数
    private int loopNumber;

    public WaitNotify(int flag, int loopNumber) {
        this.flag = flag;
        this.loopNumber = loopNumber;
    }

    /**
     * 打印方法
     * @param str      输出内容
     * @param waitFlag 当前等待标记
     * @param nextFlag 下一个等待标记
     */
    public void print(String str, int waitFlag, int nextFlag) {
        for (int i = 0; i < loopNumber; i++) {
            synchronized (this) {
                while (flag != waitFlag) {
                    try {
                        this.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.print(str);
                flag = nextFlag;
                this.notifyAll();
            }
        }
    }

    ;
}

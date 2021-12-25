package com.atguigu.www.lock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 使用Lock的Demo
 * java操作一个资源，要先new一个资源类，实体变量+实体方法，方法即暴露对线程操作的接口
 * 卖票Demo,多个线程使用Lock同步卖票
 *
 * Lock实现提供比使用synchronized方法和语句可以获得的更广泛的锁定操作。
 * 们允许更灵活的结构化，可能具有完全不同的属性，并且可以支持多个相关联的对象Condition 。
 */
//第一步  创建资源类，定义属性和和操作方法
class LTicket {
    //票数量
    private int number = 30;

    //创建可重入锁
    private final ReentrantLock lock = new ReentrantLock(true);
    //卖票方法
    public void sale() {
        //上锁
        lock.lock();
        try {
            //判断是否有票
            if(number > 0) {
                System.out.println(Thread.currentThread().getName()+":现有票数："+(number--)+"，卖出一张票， 剩下："+number);
            }
        } finally {
            //解锁
            lock.unlock();
        }
    }
}

public class LSaleTicket {
    //第二步 创建多个线程，调用资源类的操作方法
    //创建三个线程
    public static void main(String[] args) {

        LTicket ticket = new LTicket();

        new Thread(()-> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"AA").start();

        new Thread(()-> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"BB").start();

        new Thread(()-> {
            for (int i = 0; i < 40; i++) {
                ticket.sale();
            }
        },"CC").start();
    }
}

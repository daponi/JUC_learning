package com.atguigu.www.sync;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 简单的可重入锁Demo
 */
@SuppressWarnings({"all"})
public class SyncLockDemo {

    //可重入锁，若没有Synchronized则是递归调用
    public synchronized void add() {
        add();
    }

    public static void main(String[] args) {
        //Lock演示可重入锁
        Lock lock = new ReentrantLock();
        //创建线程
        new Thread(()->{
            try {
                //上锁
                lock.lock();
                System.out.println(Thread.currentThread().getName()+" 外层");
                try {
                    //上锁
                    lock.lock();
                    System.out.println(Thread.currentThread().getName()+" 内层");
                }finally {
                    //若注释掉 ,由于加锁次数和释放次数不一样，第二个线程始终无法获取到锁，导致一直在等待。
                    // lock.unlock();
                }
            }finally {
                //释放做
                lock.unlock();
            }
        },"t1").start();

        //创建新线程
        new Thread(()->{
            lock.lock();
            System.out.println("aaaa");
            lock.unlock();
        },"aa").start();
       // new SyncLockDemo().add();
       // synchronized
//        Object o = new Object();
//        new Thread(()->{
//            synchronized(o) {
//                System.out.println(Thread.currentThread().getName()+" 外层");
//
//                synchronized (o) {
//                    System.out.println(Thread.currentThread().getName()+" 中层");
//
//                    synchronized (o) {
//                        System.out.println(Thread.currentThread().getName()+" 内层");
//                    }
//                }
//            }
//
//        },"t1").start();
    }

}

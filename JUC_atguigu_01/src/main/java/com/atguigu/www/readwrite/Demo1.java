package com.atguigu.www.readwrite;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 演示ReentrantReadWriteLock的读写锁降级
 */

public class Demo1 {

    public static void main(String[] args) {
        //可重入读写锁对象
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        ReentrantReadWriteLock.ReadLock readLock = rwLock.readLock();//读锁
        ReentrantReadWriteLock.WriteLock writeLock = rwLock.writeLock();//写锁

        //锁降级，同一个线程可先获取写锁再获取读锁，但不能先获取读锁再获取写锁
        //1 获取写锁
        writeLock.lock();
        System.out.println("atguigu");
        //2 获取读锁
        readLock.lock();
        System.out.println("---read");
        //3 释放写锁
        writeLock.unlock();
        //4 释放读锁
        readLock.unlock();
    }
}

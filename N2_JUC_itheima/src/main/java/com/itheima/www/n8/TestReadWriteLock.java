package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *JAVA 的并发包提供了读写锁 ReentrantReadWriteLock， 它表示两个锁，一个是读操作相关的锁，称为共享锁；一个是写相关的锁，称为排他锁
 * 提供一个内部分别使用读锁保护数据的方法，写锁保护数据的方法
 * 1. 线程进入读锁的前提条件：
 * ● 没有其他线程的写锁
 * ● 没有写请求, 或者==有写请求，但调用线程和持有锁的线程是同一个(可重入锁)。==
 * 2. 线程进入写锁的前提条件：
 * ● 没有其他线程的读锁
 * ● 没有其他线程的写锁
 * 而读写锁有以下三个重要的特性：
 *   a. 公平选择性：支持非公平（默认）和公平的锁获取方式，吞吐量还是非公平优于公平。
 *   b. 重进入：读锁和写锁都支持线程重进入。
 *   c. 锁降级：遵循获取写锁、获取读锁再释放写锁的次序，写锁能够降级成为读锁。
 *
 * 注意：
 * 读锁不支持条件变量
 * 重入时升级不支持：即持有读锁的情况下去获取写锁，会导致获取写锁永久等待
 */
@Slf4j
public class TestReadWriteLock {
    public static void main(String[] args) {
        DataContainer data = new DataContainer();
        new Thread(()->{
            data.read();
            // data.write(2);
        }).start();
        new Thread(()->{
            data.read();
            // data.write(2);
        }).start();
        new Thread(()->{
            data.read();
            // data.write(2);
        }).start();
    }
}

@Slf4j
class DataContainer{
    private int data;
    private ReentrantReadWriteLock readWriteLock=new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock=readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock=readWriteLock.writeLock();

    public void setData(int data) {
        this.data = data;
    }

    public void read(){
        readLock.lock();

        try {
            log.debug("开始获得读锁...");
            log.debug("data :{}",data);
        } finally {
            readLock.unlock();
            log.debug("{} 释放读锁....",Thread.currentThread().getName());
        }
    }

    public void write(int param){
       writeLock.lock();

        try {
            log.debug("开始获得写锁...");
            this.setData(param);
            Sleeper.sleep(2);
            log.debug("data :{}",data);
        } finally {
            writeLock.unlock();
            log.debug("{} 释放写锁....",Thread.currentThread().getName());
        }

    }
}

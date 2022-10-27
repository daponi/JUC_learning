package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * sleep(long n)和wait(long n)的区别
 * 1) sleep 是 Thread 方法，而 wait 是 Object 的方法
 * 2) sleep 不需要强制和 synchronized 配合使用，但 wait 需要和 synchronized 一起用
 * 3) sleep 在睡眠的同时，不会释放对象锁的，但 wait 在等待的时候会释放对象锁
 * 4) 它们状态 TIMED_WAITING，它们都会释放CPU且都能被interrupted方法打断，sleep自动结束被唤醒，而wait是进入waitSet其它等待线程被唤醒
 */
@Slf4j
public class Test19 {
    static final Object lock =new Object();//final保证变量引用对象的地址不能变，引用变量所指的对象的内容可以改变。
    public static void main(String[] args) throws InterruptedException {
        Thread t1=new Thread(()->{
            synchronized(lock){
                log.debug("获得锁");
                try {
                    // Thread.sleep(4000);
                    lock.wait(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"t1");
        t1.start();

        System.out.println(t1.getState());
        Thread.sleep(2000);
        System.out.println(t1.getState()); //TIMED_WAITING
        synchronized(lock){
            log.debug("获得锁.");
        }
    }
}

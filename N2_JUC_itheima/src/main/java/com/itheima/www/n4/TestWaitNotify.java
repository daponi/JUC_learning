package com.itheima.www.n4;


import lombok.extern.slf4j.Slf4j;

/**
 *
 * obj.wait() 让进入 object 监视器的线程到 waitSet 等待
 * obj.notify() 在 object 上正在 waitSet 等待的线程中挑一个唤醒
 * obj.notifyAll() 让 object 上正在 waitSet 等待的线程全部唤醒
 *
 * 它们都是线程之间进行协作的手段，都属于 Object 对象的方法，是重量级锁(10，即monitor)
 * 必须获得此对象的锁即在synchronized内才能调用这几个方法
 */
@Slf4j
public class TestWaitNotify {
    final static Object OBJ =new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            synchronized(OBJ){
                log.debug("正在执行......");
                try {
                    OBJ.wait(); // 让线程t1在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t1其它代码......");
            }
        },"t1").start();

        new Thread(()->{
            synchronized(OBJ){
                log.debug("正在执行......");
                try {
                    OBJ.wait(); // 让线程t1在obj上一直等待下去
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("t2其它代码......");
            }
        },"t2").start();

        Thread.sleep(2000);
        log.debug("开始唤醒obj上的其它线程");
        synchronized(OBJ){
           // OBJ.notify(); // 唤醒obj上一个线程
            OBJ.notifyAll(); // 唤醒obj上所有等待线程
        }
    }
}

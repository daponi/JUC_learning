package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 使用interrupt()打断线程sleep，并查看状态
 */
@Slf4j(topic = "c.Test7")
public class Test7 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("enter sleep...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    log.debug("wake up...");
                    if (this.isInterrupted()) //不进入该循环，interruptted()将线程的中断标志isInterrupted()重新设置为false.
                        log.debug("t1 state 4: {}", this.getState());
                    if (!Thread.interrupted()) {
                        log.debug("t1 state 5: {}", this.getState());//RUNNABLE
                    }
                    e.printStackTrace();
                }
            }
        };
        t1.start();

        log.debug("t1 state 1: {}", t1.getState());  // RUNNABLE
        Thread.sleep(1000);
        log.debug("t1 state 2: {}", t1.getState()); // TIMED_WAITING
        log.debug("interrupt...");
        t1.interrupt();
        log.debug("t1 state 3: {}", t1.getState()); //线程t1还没被interrupt打断就先执行，所以此时还是TIMED_WAITING
        Thread.sleep(1);
        // t1.join();
        log.debug("t1 state 6: {}", t1.getState()); //主线程main先睡眠1毫秒等线程t1先被中断sleep()，此时t1是RUNNABLE

    }
}

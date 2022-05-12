package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 线程状态又RUNNABLE到TIMED_WAITING
 */
@Slf4j(topic = "c.Test6")
public class Test6 {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        t1.start();
        log.debug("t1 state: {}", t1.getState());// 比run()方法先执行，所以是RUNNABLE

        try {
            Thread.sleep(500); // 让t1线程先执行sleep()进入TIMED_WAITING
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 state: {}", t1.getState());
    }
}

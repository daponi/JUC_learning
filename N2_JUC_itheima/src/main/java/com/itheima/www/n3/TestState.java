package com.itheima.www.n3;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;


/**
 * 演示java线程的6种状态
 */
@Slf4j(topic = "c.TestState")
public class TestState {
    public static void main(String[] args) throws IOException {
        // 1.new状态
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };

        // 2.runnable
        Thread t2 = new Thread("t2") {
            @Override
            public void run() {
                while(true) { // runnable

                }
            }
        };
        t2.start();

        // 3.terminated状态
        Thread t3 = new Thread("t3") {
            @Override
            public void run() {
                log.debug("running...");
            }
        };
        t3.start();

        // 4.timed_waiting
        Thread t4 = new Thread("t4") {
            @Override
            public void run() {
                synchronized (TestState.class) {
                    try {
                        Thread.sleep(1000000); // timed_waiting
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t4.start();

        // 5.waiting状态
        Thread t5 = new Thread("t5") {
            @Override
            public void run() {
                try {
                    t2.join(); // waiting
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        t5.start();

        // blocked状态
        Thread t6 = new Thread("t6") {
            @Override
            public void run() {
                synchronized (TestState.class) { // t4先拿到类锁，此时t6是 blocked
                    try {
                        Thread.sleep(1000000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t6.start();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.debug("t1 state {}", t1.getState());// NEW
        log.debug("t2 state {}", t2.getState());// RUNNABLE
        log.debug("t3 state {}", t3.getState());// TERMINATED
        log.debug("t4 state {}", t4.getState());// TIMED_WAITING
        log.debug("t5 state {}", t5.getState());// WAITING
        log.debug("t6 state {}", t6.getState());// BLOCKED
        System.in.read();
    }
}

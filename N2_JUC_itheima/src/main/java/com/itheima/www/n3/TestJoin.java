package com.itheima.www.n3;

import lombok.extern.slf4j.Slf4j;

import static com.itheima.www.n2.util.Sleeper.sleep;


/**
 * Join方法的使用，
 * Thread的join方法：
 * join方法是Thread类中的一个方法，该方法的定义是等待该线程执行直到终止。其实就说join方法将挂起调用线程的执行，直到被调用的对象完成它的执行。
 * ● 调用线程：当前线程，即调用了﻿t.join()﻿语句的线程，
 * ● 被调用的对象：t.join当中的线程对象t
 *
 * 结果消耗2s
 */
@Slf4j(topic = "c.TestJoin")
public class TestJoin {
    static int r = 0;
    static int r1 = 0;
    static int r2 = 0;

    public static void main(String[] args) throws InterruptedException {
        // test1();
        // test2();
        // test3();
        test4();

    }

    private static void test4() {
        Thread t1 =new Thread(()->{
            log.debug("t1 start。。。");
            sleep(15);
            log.debug("t1 end。。。");
        },"t1");
        t1.start();

        Thread t2 =new Thread(()->{

            log.debug("t2 start。。。");
            try {
                t1.join();
            } catch (InterruptedException e) {
                log.debug("t2 interrupted！！！");
                e.printStackTrace();
            }
            log.debug("t2 end。。。");

        },"t2");
        t2.start();

        sleep(2);
        log.debug("main sleep。。。。");
        t2.interrupt();
        // t1.join();
        log.debug("main end》》》");
    }

    public static void test3() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(2);
            r1 = 10;
        });

        long start = System.currentTimeMillis();
        t1.start();

        log.debug("join begin");
        // join设置最多等待时间，线程可提前结束，线程执行结束会导致 join 结束
        // t1.join(3000);
        t1.join(1500);
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

    private static void test2() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            sleep(1); // 睡眠1秒
            r1 = 10;
        });
        Thread t2 = new Thread(() -> {
            sleep(2); //睡眠2秒
            r2 = 20;
        });
        t1.start();
        t2.start();
        long start = System.currentTimeMillis();
        log.debug("join begin");
        t2.join();
        log.debug("t2 join end");
        t1.join();
        log.debug("t1 join end");
        long end = System.currentTimeMillis();
        log.debug("r1: {} r2: {} cost: {}", r1, r2, end - start);
    }

    private static void test1() throws InterruptedException {
        log.debug("开始");
        Thread t1 = new Thread(() -> {
            log.debug("开始");
            sleep(1);
            log.debug("结束");
            r = 10;
        });
        t1.start();
        t1.join();
        log.debug("结果为:{}", r);
        log.debug("结束");
    }
}

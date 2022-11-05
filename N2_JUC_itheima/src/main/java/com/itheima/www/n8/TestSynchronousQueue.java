package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * newCachedThreadPool
 * 特点：核心线程数是 0， 最大线程数是 Integer.MAX_VALUE，救急线程的空闲生存时间是 60s，意味着全部都是救急线程（60s 后可以回收），救急线程可以无限创建
 *
 * 内部的阻塞队列使用了SynchronousQueue，其特点是它没有容量，没有线程来取是放不进去的（一手交钱、一手交货）
 * 一个线程存进去后就阻塞，等人取出来后才被唤醒能继续存进去
 */
@Slf4j(topic = "c.TestSynchronousQueue")
public class TestSynchronousQueue {
    public static void main(String[] args) {
/*        ExecutorService pool = Executors.newCachedThreadPool();
        pool.execute(() -> {
            log.debug("111");
                log.debug("222");});
        pool.execute(() -> {
            log.debug("33");
            log.debug("444");});
        pool.execute(() -> {
            log.debug("555");
            log.debug("666");});*/

        SynchronousQueue<Integer> integers = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                log.debug("putting {} ", 1);
                integers.put(1);
                log.debug("{} putted...", 1);

                log.debug("putting...{} ", 2);
                integers.put(2);
                log.debug("{} putted...", 2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1").start();

        sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 1);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t2").start();

        sleep(1);

        new Thread(() -> {
            try {
                log.debug("taking {}", 2);
                integers.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t3").start();
    }
}

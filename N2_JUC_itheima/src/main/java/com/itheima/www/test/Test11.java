package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * sleep、wait、join后
 * interrupt的使用会将打断标记isInterrupted设置为true，但sleep除外它将打断标记设置为true但sleep会删出打断标记扔为false
 */
@Slf4j(topic = "c.Test11")
public class Test11 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            log.debug("sleep...");
            try {
                Thread.sleep(5000); // wait, join被打断会将打断标记设置为true但sleep会删出打断标记扔为false
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"t1");

        t1.start();
        Thread.sleep(1000); //让t1先sleep再interrupt
        log.debug("interrupt");
        log.debug("打断前的标记{}", t1.isInterrupted());
        t1.interrupt();
        log.debug("打断标记:{}", t1.isInterrupted());
    }
}

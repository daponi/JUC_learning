package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReentrantLock相对于 synchronized 它具备如下特点
 * 1.可中断
 * 2.可以设置超时时间
 * 3.可以设置为公平锁
 * 4.支持多个条件变量
 * 与 synchronized 一样，都支持可重入
 *
 * 可重入性
 */
@Slf4j(topic = "C.ReentrantLock")
public class Test22 {
    static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        method1();
    }

    public static void method1() {
        reentrantLock.lock();
        try {
            log.debug("Starting perform method 1...");
            method2();
        } finally {
            log.debug("Releasing method 1...");
            reentrantLock.unlock();
        }
    }

    public static void method2() {
        reentrantLock.lock();
        try {
            log.debug("Starting perform method 2...");
            method3();
        } finally {
            log.debug("Releasing method 2...");
            reentrantLock.unlock();
        }
    }

    public static void method3() {
        reentrantLock.lock();
        try {
            log.debug("Starting perform method 3...");
        } finally {
            log.debug("Releasing method 3...");
            reentrantLock.unlock();
        }
    }
}

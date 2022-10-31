package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 同步模式之顺序控制
 * 比如，两个线程必须先 2 后 1 打印
 * 1.使用synochronized实现
 * 2.使用park/unpark实现
 */
@Slf4j
public class Test25 {
    static final Object obj=new Object();
    // 表示 t2 是否运行过
    static boolean has2Runn=false;
    public static void main(String[] args) {
        new Thread(()->{
            synchronized(obj){
                while (!has2Runn){
                    try {
                        obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                log.debug("1");
            }
        },"t1").start();

        new Thread(()->{
            synchronized(obj){
                log.debug("2");
                has2Runn=true;
                obj.notify();
            }
        },"t2").start();
    }
}

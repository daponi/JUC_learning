package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * 可见性的例子
 * 解决方法:
 * volatile（易变关键字）
 * 它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，
 * 线程操作 volatile 变量都是直接操作主存
 *
 * volatile只能解决可见性问题  不能解决原子性问题
 * volatile保证可见性，它保证的是在多个线程之间，一个线程对 volatile 变量的修改对另一个线程可见， 但不能保证原子性，仅用在一个写线程，多个读线程的情况
 * synchronized 语句块既可以保证代码块的原子性，也同时保证代码块内变量的可见性。但缺点是synchronized 是属于重量级操作，性能相对更低
 */
@Slf4j
public class Test32_2 {
    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        new Thread(()->{
            System.out.println("=================== come in AAA");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myNumber.changeNum();
            System.out.println("=================== The number has been changed!");
        }, "AAA").start();

        while (myNumber.number==10){
            // while里面不要println,因为println是sychronized修饰的，会影响内存可见性
            // System.out.println(" =====================main is while!");

        }
        System.out.println(" =====================main is Over!");
    }
}
class MyNumber{
    int number=10;
    //volatile int number=10;
    public void changeNum(){
        this.number=1024;
    }
}
package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 *
 *
 *  可见性的例子
 *  解决方法:
 * volatile（易变关键字）
 * 它可以用来修饰成员变量和静态成员变量，他可以避免线程从自己的工作缓存中查找变量的值，必须到主存中获取它的值，
 * 线程操作 volatile 变量都是直接操作主存
 *
 *  volatile只能解决可见性问题  不能解决原子性问题
 *  volatile保证可见性，它保证的是在多个线程之间，一个线程对 volatile 变量的修改对另一个线程可见， 但不能保证原子性，仅用在一个写线程，多个读线程的情况
 *  synchronized 语句块既可以保证代码块的原子性，也同时保证代码块内变量的可见性。但缺点是synchronized 是属于重量级操作，性能相对更低
 *
 *
 *
 * MM 即 Java Memory Model，它定义了主存、工作内存抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、CPU 指令优化等。
 * 不是二刷 , 是老师的JVM那门课也讲过这个
 * 这里想体现JMM可见性，但例子错了：这里之所以不会停止while原因是jit（Just In Time）优化把while循环语句的判断变量用真实值代替，比如while（run）替换成了while（true），无论你怎么改变量run都不会改变判断语句的true，和可见性无关。
 * 在启动JVM的时候，只需增加-Xint或者-Djava.compiler=NONE关闭JIT则发现while停止了：
 *
 * 关于编译器要知道一个新成员 Graal，从JDK10开始可以替换服务端编译器。
 * 编辑器参数加-Xint禁用JIT就能可见是因为缓存一致性协议
 */
@Slf4j
public class Test32 {
    // 项目中JIT的优化是很重要的，所以不能因为一个变量就-Xint禁用JIT，正确的解决方案是用关键字volatile
    static  boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        int i=0;
        new Thread(()->{
            try {
                Thread.sleep(1);
                // 睡眠1秒则主线程while循环太多次被JIT优化：将run直接替换为true，所以即使该线程更改变量后while仍不会被打断
                // TimeUnit.SECONDS.sleep(1);
                // 若只睡眠1毫秒则JIT还来不及优化，改变run的值仍能被主线程读取并打断while循环
                // TimeUnit.MILLISECONDS.sleep(1);
                run=false;
                log.debug("run改变了");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"T1").start();

        new Thread(()->{
            try {
                Thread.sleep(2);
                run=false;  // 验证T1更改run值后是否写入主内存中
                log.debug("访问run:{}",run);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        },"T2").start();

        while (run){
            // System.out.println(run); //sout源码是加了锁不能用这个打印
            i++; // 验证被循环了多少次
        }
        log.debug("i:{}",i);
    }
}

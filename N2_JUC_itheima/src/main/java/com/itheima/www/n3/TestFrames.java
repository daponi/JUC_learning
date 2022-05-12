package com.itheima.www.n3;

/**
 * 栈帧
 * 栈与栈帧
 * Java Virtual Machine Stacks （Java 虚拟机栈）
 * 我们都知道 JVM 中由堆、栈、方法区所组成，其中栈内存是给谁用的呢？其实就是线程，每个线程启动后，虚拟机就会为其分配一块栈内存。
 * 每个栈由多个栈帧（Frame）组成，对应着每次方法调用时所占用的内存
 * 每个线程只能有一个活动栈帧，对应着当前正在执行的那个方法
 *
 * 线程上下文切换（Thread Context Switch）
 * 因为以下一些原因导致 cpu 不再执行当前的线程，转而执行另一个线程的代码：
 * 	1.线程的 cpu 时间片用完
 * 	2.垃圾回收
 * 	3.有更高优先级的线程需要运行
 * 	4.线程自己调用了 sleep、yield、wait、join、park、synchronized、lock 等方法
 * 当 Context Switch 发生时，需要由操作系统保存当前线程的状态，并恢复另一个线程的状态，Java 中对应的概念 就是程序计数器（Program Counter Register），它的作用是记住下一条 jvm 指令的执行地址，是线程私有的状态
 * 包括程序计数器、虚拟机栈中每个栈帧的信息，如局部变量、操作数栈、返回地址等
 * Context Switch 频繁发生会影响性能
 */
public class TestFrames {
    public static void main(String[] args) {
        Thread t1 = new Thread(){
            @Override
            public void run() {
                method1(20);
            }
        };
        t1.setName("t1");
        t1.start();
        method1(10);
    }

    private static void method1(int x) {
        int y = x + 1;
        Object m = method2();
        System.out.println(m);
    }

    private static Object method2() {
        Object n = new Object();
        return n;
    }
}

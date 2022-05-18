package com.itheima.www.n4;

import java.util.ArrayList;

/**
 * 变量的线程安全分析 --局部变量与成员变量
 *
 * 局部变量是线程安全的
 * 但局部变量引用的对象则未必
 *  如果该对象没有逃离方法的作用访问，它是线程安全的
 *  如果该对象逃离方法的作用范围，需要考虑线程安全
 *
 * 【成员变量】和【局部变量】的区别吧，前者是共享资源，后者是线程私有资源
 * list作为成员变量时
 * list的add操作不保证原子性，线程1在添加的时候有可能线程2也在添加，然后就只添加了一个元素，但是接下来却碰到了两个移除0位置的操作，就报错了
 * list作为局部变量时
 * 栈中的局部变量不会有多个线程访问到，每个线程私有的，都访问自己的
 */
public class TestThreadSafe {
    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;

    public static void main(String[] args) {
        ThreadUnsafe1 test1 = new ThreadUnsafe1();
        ThreadUnsafe2 test2 = new ThreadUnsafe2();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                // test1.method1(LOOP_NUMBER);
                test2.method1(LOOP_NUMBER);
            }, "Thread" + i).start();
        }
    }
}

// 成员变量list，会报错因为有多个线程共享触发list越界
class ThreadUnsafe1 {
    ArrayList<String> list = new ArrayList<>();//成员变量list，会报错因为有多个线程共享触发list越界

    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            // { 临界区, 会产生竞态条件
            method2();
            method3();
            // } 临界区
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}

//局部变量list，线程私有资源
class ThreadUnsafe2 {

    public void method1(int loopNumber) {
        ArrayList<String> list = new ArrayList<>();//局部变量list，线程私有资源
        for (int i = 0; i < loopNumber; i++) {
            // { 临界区, 会产生竞态条件
            method2(list);
            method3(list);
            // } 临界区
        }
    }

    private void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {
        list.remove(0);
    }
}
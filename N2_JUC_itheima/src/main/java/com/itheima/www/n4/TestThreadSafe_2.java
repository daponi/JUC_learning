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
 *
 * 注意private 或 final修饰方法的重要性
 * private定义的方法是私有的，所有的private⽅法默认是final的，即不可继承的。所以当B继承A时，A的private⽅法print()不被B继承。
 * ⽽B中的public⽅法print()相当于B添加的⼀个⽅法，不属于重写。因此，我们也不能重写被private修饰的方法，同时也可在方法前加final, 用于当前类被子类继承后,不允许子类对该方法重写。
 */
public class TestThreadSafe_2 {

    static final int THREAD_NUMBER = 2;
    static final int LOOP_NUMBER = 200;
    public static void main(String[] args) {
        ThreadSafeSubClass test = new ThreadSafeSubClass();
        for (int i = 0; i < THREAD_NUMBER; i++) {
            new Thread(() -> {
                test.method1(LOOP_NUMBER);
            }, "Thread" + (i+1)).start();
        }
    }
}
// list成员变量
class ThreadUnsafe {
    ArrayList<String> list = new ArrayList<>();
    public void method1(int loopNumber) {
        for (int i = 0; i < loopNumber; i++) {
            method2();
            method3();//若是private则调用的是父类的method3
        }
    }

    private void method2() {
        list.add("1");
    }

    private void method3() {
        list.remove(0);
    }
}
//list作为局部变量
class ThreadSafe {
    public final void method1(int loopNumber) { // 添加final防止被重写
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i < loopNumber; i++) {
            method2(list);
            method3(list);
        }
    }

    public void method2(ArrayList<String> list) {
        list.add("1");
    }

    private void method3(ArrayList<String> list) {// private 访问修饰符来修饰method2、method3来避免字类对父类方法的重写，
        System.out.println(1);
        list.remove(0);
    }
}

class ThreadSafeSubClass extends ThreadSafe{

   // @Override
    public void method3(ArrayList<String> list) {
        System.out.println(2);
        new Thread(() -> {
            list.remove(0);
        }).start();
    }
}


package com.itheima.www.n4;


import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 变量的线程安全分析 --局部变量与成员变量
 * 局部变量的引用暴露给其它线程，则会造成线程安全问题
 *  SimpleDateFormat是非线程安全的
 *  抽象类TestAbstract里的bar()有局部变量，但暴露给了一个抽象方法，则该方法的实现类是不确定的，若是实现方法里又开启了新的线程就会造成线程安全问题
 *  其中 foo 的行为是不确定的，可能导致不安全的发生，被称之为外星方法
 *
 *  一、局部变量存储在方法栈中
 *        在方法中声明的变量，即该变量是局部变量，每当程序调用方法时，系统都会为该方法建立一个方法栈，其所在方法中声明的变量就放在方法栈中（具体点就是在栈帧中），当方法结束系统会释放方法栈，其对应在该方法中声明的变量随着栈的销毁而结束，这就局部变量只能在方法中有效的原因。
 * 在方法中声明的变量可以是基本类型的变量，也可以是引用类型的变量。
 * （1）当声明是基本类型的变量的时，其变量名及值（变量名及值是两个概念）是放在方法栈中
 * （2）当声明的是引用变量时，所声明的变量（该变量实际上是在方法中存储的是内存地址值）是放在方法的栈中，该变量所指向的对象是放在堆内存中的。
 *
 * 二、全局变量存储在堆中
 * 在类中声明的变量是成员变量，也叫全局变量，放在堆中的（因为全局变量不会随着某个方法执行结束而销毁）。同样在类中声明的变量即可是基本类型的变量 也可是引用类型的变量
 * （1）当声明的是基本类型的变量其变量名及其值放在堆内存中的
 * （2）引用类型时，其声明的变量仍然会存储一个内存地址值，该内存地址值指向所引用的对象。引用变量名和对应的对象仍然存储在相应的堆中.
 *
 */
public class TestThreadUnsafe extends TestAbstract{
    @Override
    public void foo(SimpleDateFormat sdf) {
        String dateStr = "1999-10-11 00:00:00";
        for (int i = 0; i < 20; i++) {
            new Thread(() -> {

                    try {
                        sdf.parse(dateStr);
                        System.out.println(sdf.parse(dateStr));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

            }).start();
        }
    }

    public static void main(String[] args) {
        new TestThreadUnsafe().bar();
    }
}
abstract class TestAbstract {

    public abstract void foo(SimpleDateFormat sdf);

    public void bar() {
        // 是否安全
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        foo(sdf); //将局部变量的引用暴露出去了，不安全
    }

}

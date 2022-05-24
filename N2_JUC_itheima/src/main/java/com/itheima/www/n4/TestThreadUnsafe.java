package com.itheima.www.n4;


import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * 变量的线程安全分析 --局部变量与成员变量
 * 局部变量的引用暴露给其它线程，则会造成线程安全问题
 *  SimpleDateFormat是非线程安全的
 *  抽象类TestAbstract里的bar()有局部变量，但暴露给了一个抽象方法，则该方法的实现类是不确定的，若是实现方法里又开启了新的线程就会造成线程安全问题
 *  其中 foo 的行为是不确定的，可能导致不安全的发生，被称之为外星方法
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

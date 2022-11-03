package com.itheima.www.n5;

/**
 * 获取final变量的原理:
 * 其它方法使用final修饰的常量时是直接将它的值push到自己的栈上，不会引用变量，
 * 若不加final则是用GETSTATIC获取类的变量，再取值
 *
 其他类引用另一个类的static final变量时，数字比较小则将值(不含变量)直接push到自己的栈内存，若数字超过了该类型(如int、short)的最大值则到常量池LDC加载，
 若不加final则该类需要从堆内存中访问，速度更慢。
 */
public class TestFinal {
    static final int A = 10;
    static final int B = Short.MAX_VALUE+1;

    final int a = 20;
    final int b = Integer.MAX_VALUE;

    final void test1() {
        final int c = 30;
        new Thread(()->{
            System.out.println(c);
        }).start();

        final int d = 30;
        class Task implements Runnable {

            @Override
            public void run() {
                System.out.println(d);
            }
        }
        new Thread(new Task()).start();
    }

}

class UseFinal1 {
    public void useTest() {
        System.out.println(TestFinal.A);
        System.out.println(TestFinal.B);
        System.out.println(new TestFinal().a);
        System.out.println(new TestFinal().b);
        new TestFinal().test1();
    }
}

class UseFinal2 {
    public void test() {
        System.out.println(TestFinal.A);
    }
}
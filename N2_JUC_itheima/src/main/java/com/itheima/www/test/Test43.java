package com.itheima.www.test;

import java.util.concurrent.TimeUnit;

/**
 * 强引用：最常见
 * 当内存不足,JVM开始垃圾回收,对于强引用的对象,就算是出现了OOM也不会对该对象进行回收,机器死了都不收。
 * 强引用是我们最常见的普通对象引用,只要还有强引用指向一个对象,就能表明对象还“活着”,垃圾收集器不会碰这种对象。 在Java 中最常见的就是强引用,
 * 把一个对象赋给一个引用变量,这个引用变量就是一个强引用。 当一个对象被强引用变量引用时,它处于可达状态,它是不可能被垃圾回收机制回收的, 即使该对象以后永远都不会被用到,JVM也不会回收。
 * 因此强引用是造成Java内存泄漏的主要原因之一。
 * 对于一个普通的对象,如果没有其他的引用关系,只要超过了引用的作用域或者显式地将相应(强)引用赋值为 null一般认为才是可以被垃圾收集的了(当然具体回收时机还是要看垃圾收集策略)。
 */
public class Test43 {
    //这个方法一般不用复写，我们只是为了教学给大家演示案例做说明
    @Override
    protected void finalize() throws Throwable {
        // finalize的通常目的是在对象被不可撤销地丢弃之前执行清理操作。
        System.out.println("-------invoke finalize method~!!!");
    }


}

class ReferenceDemo {
    public static void main(String[] args) {
        Test43 myObject = new Test43();
        System.out.println("gc before: " + myObject);
        myObject = null;
        System.gc();//人工开启GC，一般不用
        //暂停毫秒
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("gc after: " + myObject);
    }
}
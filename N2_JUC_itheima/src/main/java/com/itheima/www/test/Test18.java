package com.itheima.www.test;

/**
 * wait()和notify()、notifyAll()方法都是java.lang.Object的方法。
 * 必须获得此对象的锁，才能调用这几个方法，所以必须在Synchronized语句块内使用
 */
public class Test18 {
        static final Object o=new Object();
    public static void main(String[] args) throws InterruptedException {
        synchronized (o){
        o.wait();// Exception in thread "main" java.lang.IllegalMonitorStateException
        }
    }
}

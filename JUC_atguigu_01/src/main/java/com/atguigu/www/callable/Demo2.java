package com.atguigu.www.callable;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * 通过Callable接口创建线程
 * FutureTask类实现Callable接口
 */
public class Demo2 {

    //实现Runnable接口
    static class MyThread1 implements Runnable {
        @Override
        public void run() {
            try {
                System.out.println(Thread.currentThread().getName() + "线程进入了 run方法");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    //实现Callable接口
    static class MyThread2 implements Callable {
        @Override
        public Long call() throws Exception {
            try {
                System.out.println(Thread.currentThread().getName() + "线程进入了 call方法,开始准备睡觉");
                Thread.sleep(1000);
                System.out.println(Thread.currentThread().getName() + "睡醒了");
            }catch (Exception e){
                e.printStackTrace();
            }
            return System.currentTimeMillis();//callabe接口的call方法计算任务
        }
    }

    public static void main(String[] args) throws Exception{
        //声明 runable
        Runnable runable = new MyThread1();
        //声明 callable
        Callable callable = new MyThread2();
        //future-callable
        FutureTask<Long> futureTask2 = new FutureTask(callable);
        //线程二.通过Callable接口创建Thread
        new Thread(futureTask2, "线程二").start();
        //结果只计算了一次，多次get()结果不变
        for (int i = 0; i < 10; i++) {
            Long result1 = futureTask2.get();//Future接口的get()方法将结果返回到主线程
            System.out.println(result1);
        }
        //线程一
        new Thread(runable,"线程一").start();
    }
}



package com.atguigu.www.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 演示线程池三种常用分类
 * 模拟多个窗口办理业务
 */
public class ThreadPoolDemo1 {
    public static void main(String[] args) {
        //newFixedThreadPool固定5个线程，一池五线程
        ExecutorService threadPool1 = Executors.newFixedThreadPool(5); //5个窗口

        //newSingleThreadExecutor，一池一线程
        ExecutorService threadPool2 = Executors.newSingleThreadExecutor(); //一个窗口

        //newCachedThreadPool，一池可扩容线程
        ExecutorService threadPool3 = Executors.newCachedThreadPool();//可变的多个窗口
        //10个顾客请求
        try {
            for (int i = 1; i <= 20; i++) {
                //执行
                threadPool3.execute(() -> {
                    System.out.println(Thread.currentThread().getName() + " 办理业务");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //线程用完需要放回去，关闭线程池
            threadPool3.shutdown();
        }
    }
}

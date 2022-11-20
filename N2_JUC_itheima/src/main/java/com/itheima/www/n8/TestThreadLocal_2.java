package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 【强制】必须回收自定义的ThreadLocal变量，尤其在线程池场景下，线程经常会被复用，
 * 如果不清理自定义的 ThreadLocal变量，可能会影响后续业务逻辑和造成内存泄露等问题。尽量在代理中使用try-finally块进行回收。 例：
 * 保持每个线程输出beforeInt:0	 afterInt: 1，但若不回收自定义ThreadLocal变量则会混乱
 */
@Slf4j
public class TestThreadLocal_2 {
    public static void main(String[] args) throws InterruptedException
    {
        MyData myData = new MyData();
        ExecutorService threadPool = Executors.newFixedThreadPool(3);
        try
        {
            for (int i = 0; i < 10; i++) {
                threadPool.submit(() -> {
                    try {
                        Integer beforeInt = myData.threadLocalField.get();
                        myData.add();
                        Integer afterInt = myData.threadLocalField.get();
                        log.debug(Thread.currentThread().getName()+"\t"+"beforeInt:"+beforeInt+"\t afterInt: "+afterInt);
                    } finally {
//                        myData.threadLocalField.remove();//回收自定义的 ThreadLocal 变量
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            threadPool.shutdown();
        }
    }
}
class MyData
{
    ThreadLocal<Integer> threadLocalField = ThreadLocal.withInitial(() -> 0);
    public void add()
    {
        threadLocalField.set(1 + threadLocalField.get());
    }
}
package com.atguigu.www.juc;

import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Semaphore的演示Demo
 * 6辆汽车，停3个车位
 */

@SuppressWarnings({"all"})
public class SemaphoreDemo {
    public static void main(String[] args) {
        //创建Semaphore，设置许可数量为3，相当于停车位为3
        Semaphore semaphore = new Semaphore(3);

        //创建6个线程模拟6辆汽车
        for (int i = 1; i <=6; i++) {
            new Thread(()->{
                try {
                    //抢占，若达到了许可证数量则其它线程会阻塞等待，semaphore.acquire(int i);一次获得i个许可
                    semaphore.acquire();
                    System.out.println(Thread.currentThread().getName()+" 抢到了车位");

                    //设置随机停车时间为5秒内
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));

                    System.out.println(Thread.currentThread().getName()+" ------离开了车位");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //释放
                    semaphore.release();
                }
            },String.valueOf(i)).start();
        }
    }
}

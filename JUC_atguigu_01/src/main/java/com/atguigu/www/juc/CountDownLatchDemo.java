package com.atguigu.www.juc;

import java.util.concurrent.CountDownLatch;

/**
 * 演示 CountDownLatch的Demo
 *  6 个同学陆续离开教室后值班同学才可以关门
 */
@SuppressWarnings({"all"})
public class CountDownLatchDemo {
    //6个同学陆续离开教室之后，班长锁门
    public static void main(String[] args) throws InterruptedException {

        //创建CountDownLatch对象，设置初始值
        CountDownLatch countDownLatch = new CountDownLatch(6);

        //6个同学陆续离开教室之后
        for (int i = 1; i <=6; i++) {
            new Thread(()->{
                System.out.println(Thread.currentThread().getName()+" 号同学离开了教室");

                //计数器减一,不会阻塞
                countDownLatch.countDown();

            },String.valueOf(i)).start();
        }

        //等待,当计数器为0时此处的线程会自动被唤醒
        countDownLatch.await();

        System.out.println(Thread.currentThread().getName()+" 班长锁门走人了");
    }
}

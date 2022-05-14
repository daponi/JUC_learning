package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 测试Thread的优先级，默认优先级为5
 * 程优先级会提示（hint）调度器优先调度该线程，但它仅仅是一个提示，调度器可以忽略它
 * 如果 cpu 比较忙，那么优先级高的线程会获得更多的时间片，但 cpu 闲时，优先级几乎没作用
 */
@Slf4j(topic = "c.Test9")
public class Test9_2 {

        public static void main(String[] args) {
            Runnable task1 = () -> {
                int count = 0;
                for (;;) {
                    System.out.println("---->1 " + count++);
                }
            };
            Runnable task2 = () -> {
                int count = 0;
                for (;;) {
//                Thread.yield();
                    System.out.println("              ---->2 " + count++);
                }
            };
            Thread t1 = new Thread(task1, "t1");
            Thread t2 = new Thread(task2, "t2");
            t1.setPriority(Thread.MIN_PRIORITY); //设置最小优先级
            t2.setPriority(Thread.MAX_PRIORITY);//设置最大优先级
            t1.start();
            t2.start();
        }

}

package com.itheima.www.n3;

import com.itheima.www.Constants;
import com.itheima.www.n2.util.FileReader;

/**
 * 演示java线程的runnable状态包括操作系统的阻塞状态
 */
public class TestState2 {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            FileReader.read(Constants.MP4_FULL_PATH);//进行BIO时操作系统线程看是阻塞状态，但java线程是Runnable状态
        }, "t1").start();

        Thread.sleep(1000);
        System.out.println("ok");
    }
}

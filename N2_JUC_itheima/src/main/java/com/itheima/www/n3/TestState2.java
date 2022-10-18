package com.itheima.www.n3;

import com.itheima.www.Constants;
import com.itheima.www.n2.util.FileReader;

/**
 * 演示java线程的runnable状态包括操作系统的阻塞状态
 * 在操作系统方面，读取文件属于BIO的阻塞状态，但在java方面整个程序仍是Runnable状态
 */
public class TestState2 {
    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            FileReader.read(Constants.MP4_FULL_PATH);//使用阻塞API的FileInputStream读取文件，进行BIO时操作系统线程看是阻塞状态，但java线程是Runnable状态
        }, "t1").start();

        Thread.sleep(1000);
        System.out.println("ok");
    }
}

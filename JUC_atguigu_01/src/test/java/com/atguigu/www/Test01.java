package com.atguigu.www;

import org.junit.Test;

public class Test01 {
    @Test
    public void test01(){
        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().isDaemon());
            while (true){
                //保持用户线程一直存在而不结束
            }
        },"Thread01");
        thread.start();

        System.out.println(Thread.currentThread().getName()+"\t : over");
    }
}

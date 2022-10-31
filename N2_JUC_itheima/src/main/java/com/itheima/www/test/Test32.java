package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * MM 即 Java Memory Model，它定义了主存、工作内存抽象概念，底层对应着 CPU 寄存器、缓存、硬件内存、CPU 指令优化等。
 * 不是二刷 , 是老师的JVM那门课也讲过这个
 * 这里想体现JMM可见性，但例子错了：这里之所以不会停止while原因是jit（Just In Time）优化把while循环语句的判断变量用真实值代替，比如while（run）替换成了while（true），无论你怎么改变量run都不会改变判断语句的true，和可见性无关。
 * 在启动JVM的时候，只需增加-Xint或者-Djava.compiler=NONE关闭JIT则发现while停止了：
 */
@Slf4j
public class Test32 {
    static  boolean run = true;
    public static void main(String[] args) throws InterruptedException {
        new Thread(()->{
            while (run){
                // System.out.println(run); //sout源码是加了锁不能用这个打印
            }
        }).start();
        TimeUnit.SECONDS.sleep(1);
        log.debug("run改变了");
        run=false;
    }
}

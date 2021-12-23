package com.atguigu.www.forkjoin;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;

/**
 * Fork/Join的演示Demo
 * 计算斐波纳契数字:
 * 当n=0时，f(n)=0
 * 当n=1时，f(n)=1
 * 当n>1时，f(n)=f(n-1) + f(n-2)
 */


public class ForkJoinDemo02 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        int n =15;
        System.out.println(Thread.currentThread().getName()+"线程开始 ：" + LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        Integer result = new ForkJoinPool(100).invoke(new Fibonacci(n));
        System.out.println("f("+n+")="+result);
        System.out.println(Thread.currentThread().getName()+"线程结束 ：" + LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
class Fibonacci extends RecursiveTask<Integer> {
    final int n;
    Fibonacci(int n) { this.n = n; }
    public Integer compute() {
        if (n <= 1)
            return n;
        //进行递归
        Fibonacci f1 = new Fibonacci(n - 1);
        // 调用方法拆分
        f1.fork();
        //进行递归
        Fibonacci f2 = new Fibonacci(n - 2);
        return f2.compute() + f1.join();
    }
}

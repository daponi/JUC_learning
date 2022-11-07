package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 使用ForkJoinPool线程池求1~n的和
 *
 * 思想是分治，实现是二分 ，递归不一定能并行，这个是并行的，不依赖前面的结果，看递归出口，只和begin，end相关，本来是一个个拆 现在拆成两个 就可以并行
 */
@Slf4j
public class TestForkJoin_2 {
    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool(5);
        AddTest2 addTest2 = new AddTest2(1, 5);
        System.out.println(pool.invoke(addTest2));
    }
}

@Slf4j
class AddTest2 extends RecursiveTask<Integer>{
    private int begin;
    private int end;

    public AddTest2(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    @Override
    public String toString() {
        return "{" +
                "begin=" + begin +
                ", end=" + end +
                '}';
    }

    @Override
    protected Integer compute() {
        if (begin==end) {
            log.debug("join() {}", begin);
            return begin;
        }
        if (end-1==begin) {
            log.debug("join() {} + {} = {}", begin, end, end + begin);
            return begin+end;
        }

        int mid =(begin+end)/2;
        AddTest2 t1= new AddTest2(begin, mid);
        AddTest2 t2= new AddTest2(mid+1, end);
        t1.fork();
        t2.fork();
        log.debug("fork() {} + {} + {} = ?", mid, t1, t2);
        int result =t1.join()+t2.join();
        log.debug("join() {} + {} + {} = {}", mid, t1, t2, result);
        return result;
    }
}

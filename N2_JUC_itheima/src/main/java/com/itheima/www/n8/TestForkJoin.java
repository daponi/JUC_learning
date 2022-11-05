package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * 使用ForkJoinPool线程池求1~n的和
 */
@Slf4j
public class TestForkJoin {
    public static void main(String[] args) {
        ForkJoinPool pool=new ForkJoinPool(5);
        System.out.println(pool.invoke(new AddTask(5)));

    }
}

@Slf4j
class AddTask extends RecursiveTask<Integer>{
    private  int  i;

    public AddTask(int i) {
        this.i = i;
    }

    @Override
    public String toString() {
        return "AddTask{" +
                "i=" + i +
                '}';
    }

    @Override
    protected Integer compute() {
        // 如果 n 已经为 1，可以求得结果了
        if (i==1) {
            log.debug("join(){}",i);
            return 1;
        }
        // 将任务进行拆分(fork)
        AddTask task1 = new AddTask(i-1);
        task1.fork();// 调用其它线程计算
        log.debug("fork(){}+{}",i,task1);

        // 合并(join)结果
        int result=i +task1.join(); //汇聚计算结果
        log.debug("join(){}+{}={}",i,task1,result);
        return result;
    }

}

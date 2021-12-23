package com.atguigu.www.forkjoin;

import java.util.concurrent.*;

/**
 * Fork/Join的演示Demo
 * 将1+2+3+...+100二分拆开分别计算
 * 二分查找、归并排序
 */

//RecursiveTask:用于有返回结果的任务,RecursiveAction：用于没有返回结果的任务
class MyTask extends RecursiveTask<Integer> {

    //拆分差值不能超过10，计算10以内运算
    private static final Integer VALUE = 10;
    private int begin;//拆分开始值
    private int end;//拆分结束值
    private int result; //返回结果

    //创建有参数构造
    public MyTask(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }

    //拆分和合并过程
    @Override
    protected Integer compute() {
        //判断相加两个数值是否大于10
        if ((end - begin) <= VALUE) {
            //相加操作
            for (int i = begin; i <= end; i++) {
                result = result + i;
            }
        } else {//进一步拆分
            //获取中间值
            int middle = (begin + end) / 2;
            //分成左右两块小任务，递归
            MyTask task01 = new MyTask(begin, middle);
            MyTask task02 = new MyTask(middle + 1, end);
            //执行:异步
            task01.fork();
            task02.fork();
            //同步阻塞获取执行结果
            result = task01.join() + task02.join();
        }
        return result;
    }
}

public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //创建MyTask对象，定义任务
        MyTask myTask = new MyTask(0, 100);
        //创建分支合并池对象，定义执行对象
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        //加入任务执行
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(myTask);
        //获取最终合并之后结果
        Integer result = forkJoinTask.get();
        try {
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //关闭池对象
            forkJoinPool.shutdown();
        }
    }
}

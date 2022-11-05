package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

/**
 * 1.提交任务：<T> Future<T> submit(Callable<T> task);
 * 提交任务 task，用返回值 Future 获得任务执行结果
 * Future用于保护性暂停，从领域给线程获得结果。
 *
 * 2.提交任务：<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)
 *  可以设置超时时间
 *
 * 3.提交任务： invokeAny(Collection<? extends Callable<T>> tasks)
 * 提交 tasks 中所有任务，哪个任务先成功执行完毕，返回此任务执行结果，其它任务取消
 * 底层是一个执行完成后return结果，并在finally里面遍历调用FutureTask的cancle方法
 */
@Slf4j
public class TestSubmit {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // submit();
        // invokeAll();
        invokeAny();
        log.debug("11111");
    }


    private static void submit() throws InterruptedException, ExecutionException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Future<String> future = pool.submit(new Callable<String>() {

            @Override
            public String call() throws Exception {
                log.debug("running...");
                Sleeper.sleep(2);
                return "OK...";
            }
        });
        // 调用future.get()后，main线程陷入阻塞，当结果出来后才开始向后执行
        log.debug("获得结果：{}",future.get());
        log.debug("后续");
    }

 private static void invokeAll() throws InterruptedException {
     ExecutorService pool = Executors.newFixedThreadPool(2);
     List<Future<String>> futures = pool.invokeAll(Arrays.asList(
             () -> {
                 log.debug("begining...1");
                 Sleeper.sleep(1);
                 return "111";
             },
             () -> {
                 log.debug("begining...2");
                 Sleeper.sleep(2);
                 return "222";
             },
             () -> {
                 log.debug("begining...3");
                 Sleeper.sleep(3);
                 return "333";
             }
     ));

     futures.forEach(future -> {
         try {
             log.debug("结果:{}",future.get());
         } catch (InterruptedException e) {
             e.printStackTrace();
         } catch (ExecutionException e) {
             e.printStackTrace();
         }
     });
     log.debug("end......");
 }

 private static void invokeAny() throws ExecutionException, InterruptedException {
     ExecutorService pool = Executors.newFixedThreadPool(3);
     // 只获取集合中的一个结果
     String o = pool.invokeAny(Arrays.asList(
             () -> {
                 log.debug("begining...1");
                 Thread.sleep(1000);
                 log.debug("end...1");
                 return "111";
             },
             () -> {
                 log.debug("begining...2");
                 Thread.sleep(800);
                 log.debug("end...2");
                 return "222";
             },
             () -> {
                 log.debug("begining...3");
                 Thread.sleep(2000);
                 log.debug("end...3");
                 return "333";
             }
     ));
     log.debug("结果:{}",o);
     log.debug("end.....");
 }
}

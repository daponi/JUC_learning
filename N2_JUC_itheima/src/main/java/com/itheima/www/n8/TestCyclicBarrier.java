package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

/**
 * CyclicBarrier:
 *
 * 用来进行线程协作，等待线程满足某个计数。构造时设置『计数个数』，每个线程执行到某个需要“同步”的时刻调用await()方法进行阻塞等待，当等待的线程数满足『计数个数』时，继续执行。
 * 且count减为0后cyclicBarrier对线可以继续使用，count自动重置回来，当减去的count不为0就是调用renntrantlock的await进入等待中，直到为0才会被唤醒signalAll()。
 *
 * 注意线程池的核心线程要与CyclicBarrier的parties一致，下面例子中若大于如核心线程为3则会导致循环中同时执行task1、task2、task1（第二次循环），
 * 则1秒后3个任务中两个task1先完成，触发cyclicBarrier的Runnable,与我们的期待不同
 */
@Slf4j
public class TestCyclicBarrier {
    public static void main(String[] args) throws InterruptedException {
        // test1();
        ExecutorService pool = Executors.newFixedThreadPool(2);
        // CountDownLatch countDownLatch = new CountDownLatch(2);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(2,() -> {
            log.debug("task 1 ,task 2 finish》》》");
        });

        for (int i=0;i<3;i++) {
            pool.submit(() -> {
                log.debug("task 1 begin...");
                Sleeper.sleep(1);
                log.debug("task 1 end ...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });

            pool.submit(() -> {
                log.debug("task 2 begin...");
                Sleeper.sleep(2);
                log.debug("task 2 end ...");
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        pool.shutdown();


    }

    private static void test1() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(2);

        pool.submit(() -> {
          log.debug("task 1 begin...");
            Sleeper.sleep(1);
            log.debug("task 1 end ...");
        countDownLatch.countDown();
        });

        pool.submit(() -> {
            log.debug("task 2 begin...");
            Sleeper.sleep(2);
            log.debug("task 2 end ...");
            countDownLatch.countDown();
        });

        // 不能重新计数
        pool.submit(() -> {
            log.debug("task 3 begin...");
            Sleeper.sleep(1);
            log.debug("task 3 end ...");
            countDownLatch.countDown();
        });
        countDownLatch.await();
        log.debug("task 1 ,task 2 finish》》》");
        pool.shutdown();
    }
}

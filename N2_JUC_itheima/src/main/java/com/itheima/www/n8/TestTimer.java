package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * 在『任务调度线程池』功能加入之前JDK1.5，可以使用JDK1.3的java.util.Timer来实现定时功能，
 * Timer的优点在于简单易用，但由于所有任务都是由同一个线程来调度，
 * 因此所有任务都是串行执行的，同一时间只能有一个任务在执行，前一个任务的延迟或异常都将会影响到之后的任务。
 *
 * 延时执行schedule()
 * 定时执行scheduleAtFixedRate()，若任务执行时间超过了间隔时间，任务执行完立刻进入下一轮循环
 *       scheduleWithFixedDelay()，若任务执行时间超过了间隔时间，任务执行完仍要经过间隔时间才进入下一轮循环
 *
 * 线程池里的线程默认不会打印或报异常，如何处理异常：
 * 1.直接在代码块里主动try-catch捕捉异常
 * 2.不用Runnable接口，要用Callable进行返回，用Future接受，有异常时可以future.get()获得
 */
@Slf4j(topic = "c.TestTimer")
public class TestTimer {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        method4();
        // method5();
    }

    /**
     * 使用future接受结果，可进行异常记录
     * 报异常后代码不会向下记录
     */
    private static void method4() throws InterruptedException, ExecutionException {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        ScheduledFuture<?> future = pool.schedule(() -> {
                log.debug("task1");
                int i = 1 / 0;
                log.error("end...");
        }, 1, TimeUnit.SECONDS);
        log.debug("结果:{}",future.get());
    }

    /**
     * 使用try-catch主动捕捉异常，进行日志记录
     */
    private static void method5() {
        ExecutorService pool = Executors.newFixedThreadPool(1);
        pool.submit(() -> {
            try {
                log.debug("task1");
                int i = 1 / 0;
            } catch (Exception e) {
                log.error("error:", e);
            }
        });
        // method2(pool);
        log.debug("start...");
    }

    /**
     * scheduleWithFixedDelay的使用
     */
    private static void method3() {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        log.debug("start...");
        // pool.scheduleAtFixedRate(() -> {
            pool.scheduleWithFixedDelay(() -> {
            log.debug("running...");
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * schedule的使用
     * @param pool
     */
    private static void method2(ScheduledExecutorService pool) {
        pool.schedule(() -> {
            log.debug("task1");
            int i = 1 / 0;
        }, 1, TimeUnit.SECONDS); //1秒后执行

        pool.schedule(() -> {
            log.debug("task2");
        }, 1, TimeUnit.SECONDS);
    }

    /**
     * Timer类的使用
     * 不推荐使用，是单线程顺序执行
     */
    private static void method1() {
        Timer timer = new Timer();
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 1");
                sleep(2);
            }
        };
        TimerTask task2 = new TimerTask() {
            @Override
            public void run() {
                log.debug("task 2");
            }
        };

        log.debug("start...");
        timer.schedule(task1, 1000);
        timer.schedule(task2, 1000);
    }
}

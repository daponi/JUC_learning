package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountdownLatch
 * 用来进行线程同步协作，等待所有线程完成倒计时。
 *
 * 其中构造参数用来初始化等待计数值，await()用来等待计数归零阻塞线程，countDown()用来让计数减一，计数减为0时线程唤醒
 */
@Slf4j
public class TestCountDownLatch {
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch countDownLatch = new CountDownLatch(3);
        pool.submit(() -> {
            log.debug("begin ---");
            Sleeper.sleep(1);
            countDownLatch.countDown();
            log.debug("end >>>");
        });

        pool.submit(() -> {
            log.debug("begin ---");
            Sleeper.sleep(1);
            countDownLatch.countDown();
            log.debug("end >>>");
        });

        pool.submit(() -> {
            log.debug("begin ---");
            Sleeper.sleep(1);
            countDownLatch.countDown();
            log.debug("end >>>");
        });

        pool.submit(() -> {
            log.debug("begin await，，，，，");
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("end ，，，，");
        });
    }

    private static void test1(CountDownLatch countDownLatch) {
        new Thread(()->{
          log.debug("begin ---");
          Sleeper.sleep(1);
          countDownLatch.countDown();
          log.debug("end >>>");
      }).start();
        new Thread(()->{
          log.debug("begin ---");
          Sleeper.sleep(2);
          countDownLatch.countDown();
          log.debug("end >>>");
      }).start();
        new Thread(()->{
          log.debug("begin ---");
          Sleeper.sleep(3);
          countDownLatch.countDown();
          log.debug("end >>>");
      }).start();
        log.debug("begin await，，，，，");
        try {
            countDownLatch.await();
            log.debug("end ，，，，");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

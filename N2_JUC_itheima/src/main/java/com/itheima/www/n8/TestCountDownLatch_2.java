package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

/**
 * CountDownLatch的简单应用
 * 1.10人游戏成功加载进度倒100%后才能开始游戏
 * 2.多线程分布式获取任务
 * 3.线程间交换结果用Future，CountdownLatch不能获取线程间的结果
 */
@Slf4j
public class TestCountDownLatch_2 {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        // test1();
        // test2();
        test3();
    }

    private static void test1() throws InterruptedException {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        String[] all = new String[10];
        Random random = new Random();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int j=0;j<10;j++) {
            int k=j; // lambda只能引用常量
            pool.submit(() -> {
                for (int i = 0; i <= 100; i++) {
                    all[k] = i + "%";
                    try {
                        Thread.sleep(random.nextInt(100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.print("\r" + Arrays.toString(all)); // \r回车符 就是光标回到一旧行的开头
                }
            countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println("\n 游戏开始，全军出击！");
        // 关闭线程池
        pool.shutdown();
    }

    private static void test2() throws InterruptedException, ExecutionException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("begin");
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(4);
        service.submit(() -> {
            Map<String, Object> response = restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
            log.debug("end order:{}",response);
            latch.countDown();
        });
        service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
            log.debug("end product:{}",response1);
            latch.countDown();
        });
         service.submit(() -> {
            Map<String, Object> response2 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
            log.debug("end product:{}",response2);
            latch.countDown();
        });
        service.submit(() -> {
            Map<String, Object> response3 = restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
            log.debug("end logistics:{}",response3);
            latch.countDown();
        });

        latch.await();
        log.debug("执行完毕");
        service.shutdown();
    }

    /**
     * 主线程想要拍获取返回结果，使用future则不用countdownLatch
     * 线程间交换结果用Future，CountdownLatch不能获取线程间的结果
     */
    private static void test3() throws InterruptedException, ExecutionException {
        RestTemplate restTemplate = new RestTemplate();
        log.debug("begin");
        ExecutorService service = Executors.newCachedThreadPool();
        CountDownLatch latch = new CountDownLatch(4);
        Future<Map<String,Object>> f1 = service.submit(() -> {
            Map<String, Object> response = restTemplate.getForObject("http://localhost:8080/order/{1}", Map.class, 1);
            return response;
        });
        Future<Map<String, Object>> f2 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 1);
            return response1;
        });
        Future<Map<String, Object>> f3 = service.submit(() -> {
            Map<String, Object> response1 = restTemplate.getForObject("http://localhost:8080/product/{1}", Map.class, 2);
            return response1;
        });
        Future<Map<String, Object>> f4 = service.submit(() -> {
            Map<String, Object> response3 = restTemplate.getForObject("http://localhost:8080/logistics/{1}", Map.class, 1);
            return response3;
        });

        log.debug("结果:{}",f1.get());
        log.debug("结果:{}",f2.get());
        log.debug("结果:{}",f3.get());
        log.debug("结果:{}",f4.get());
        log.debug("执行完毕");
        service.shutdown();
    }

}

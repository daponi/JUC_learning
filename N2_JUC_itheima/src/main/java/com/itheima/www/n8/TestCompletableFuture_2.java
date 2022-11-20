package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CompletableFuture;


/**
 * CompletableFuture的thenCombine使用
 */
@Slf4j
public class TestCompletableFuture_2 {
    public static void main(String[] args) {
        CompletableFuture completableFuture = CompletableFuture.supplyAsync(() -> {
            log.debug("进入任务1...");
            return "任务1";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            log.debug("进入任务2。。。");
            return "任务2";
        }), (x, y) -> {
            log.debug("x是{}，y是{}", x, y);
            return "任务3";
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            log.debug("进入第二个Combine....");
            return "任务4";
        }), (a, b) -> {
            log.debug("a是{}，b是{}", a, b);
            return "任务5结束";
        });
        log.debug("{}",completableFuture.join());

        log.debug("\n >>>>>>>>>>>>>>>>>>>>>>>>\n");

        CompletableFuture completableFuture2 =CompletableFuture.supplyAsync(()->{
            return "111";
        });

        CompletableFuture completableFuture3 =CompletableFuture.supplyAsync(()->{
            return "222";
        });

        CompletableFuture completableFuture4 = completableFuture2.thenCombine(completableFuture3, (a, b) -> {
            log.debug("a:{},b:{}", a, b);
            return "333";
        });

        log.debug("",completableFuture4.join());
    }


}

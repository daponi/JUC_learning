package com.itheima.www.n8;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Delayed;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 使用JKD的线程池，让每周四的18：00：00执行任务
 * jdk8的线程安全的时间类LocalDateTime,Duration
 */
public class TestSchedule {
    public static void main(String[] args) {
        // 当前时间
        LocalDateTime now=LocalDateTime.now();
        System.out.println(now);
        // 目标时间
        LocalDateTime targe = now.withHour(18).withMinute(0).withSecond(0).withNano(0).with(DayOfWeek.THURSDAY);
        // LocalDateTime targe = now.withHour(19).withMinute(50).withSecond(0).withNano(0).with(DayOfWeek.SATURDAY);

        // 如果当前时间>周内时间，则必须推迟到下周四
        if (now.compareTo(targe)>0) {
            targe=targe.plusWeeks(1);
        }

        // 间隔时间
        long initialDelay = Duration.between(now, targe).toMillis();

        // 执行的间隔
        long delayed=1000*60*60*24*7;
        // long delayed=1000;

        ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);
        // pool.schedule(()->{}, , )//延迟执行
        pool.scheduleWithFixedDelay(()->{
            System.out.println("runing.........");

        },initialDelay, delayed, TimeUnit.MILLISECONDS);
    }
}

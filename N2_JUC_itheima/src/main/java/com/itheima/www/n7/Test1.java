package com.itheima.www.n7;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * 不可变类的使用
 * 由于 SimpleDateFormat 不是线程安全的，有很大几率出现 java.lang.NumberFormatException 或者出现不正确的日期解析结果，
 * Java 8 后，提供了一个新的日期格式化类：DateTimeFormatter
 */
@Slf4j
public class Test1 {
    public static void main(String[] args) {
        // unsafeTest();
        DateTimeFormatter dateTimeFormatter =  DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                TemporalAccessor parse = dateTimeFormatter.parse("2022-02-22");
                log.debug("{}",parse);
            }).start();
        }
    }

    /**
     * SimpleDateFormat的parse不是线程安全的
     * 需要加Synchronized，性能太差
     */
    public static void unsafeTest() {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < 5; i++) {
        new Thread(()->{
            synchronized (sdf) {
                try {
                    Date parse = sdf.parse("2022-02-02");

                    // log.debug("{}",parse);
                    log.debug("{}",sdf.format(parse));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        }
    }
}

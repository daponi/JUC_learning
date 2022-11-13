package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * ThreadLocal：
 * 1. ThreadLocal可以实现[资源对象]的线程隔离,让每个线程各用各的[资源对象]，避免争用引发的线程安全问题
 * 2. ThreadLocal 同时实现了线程内的资源共享
 *其原理是，每个线程内有一个ThreadLocalMap类型的成员变量,用来存储资源对象
 *      ① 调用set方法,就是以ThreadLocal自己作为key,资源对象作为value,放入当前线程的ThreadLocalMap集合中
 *      ② 调用get方法，就是以ThreadLocal自己作为key,到当前线程中查找关联的资源值
 *      ③调用 remove方法，就是以ThreadLocal自己作为key,移除当前线程关联的资源值
 * 多数据源用这玩意
 *
 * 初始容量16，扩容因子2/3，扩容方法是开放寻址法而不是Map的拉链法
 * 为什么ThreadLocalMap中的key ( 即ThreadLocal )要设计为弱引用?
 * ①Thread 可能需要长时间运行(如线程池中的线程)，如果key不再使用，需要在内存不足(GC)时释放其占用的内存
 * ②但GC仅是让key的内存释放但value内存仍在，后续还要根据key是否为null来进一步释放值value的内存,释放时机有:
 *       a) get获取key发现为null的key，则释放value重新放一个key-null键值对；
 *       b) set key时，会使用启发式扫描，清除临近的null key,启发次数与元素个数、是否发现null key有关
 *       c) remove时(推荐)，因为一般使用ThreadLocal时都把它作为静态变量,因此GC无法回收
 */
@Slf4j
public class TestThreadLocal {
    public static void main(String[] args) {
        // test2();
        test1();
    }

    // 1个线程内调用，得到的是同一个Connection对象
    private static void test2(){
        for (int i = 0; i <2; i++) {
            new Thread(()->{
                log.debug("{}",Utils.getConnection());
                log.debug("{}",Utils.getConnection());
                log.debug("{}",Utils.getConnection());
            },"t"+(i+1)).start();
        }
    }

    // 多个线程内调用，得到的是自己的Connection对象
    private static void test1(){
        for (int i = 0; i < 5; i++) {
            new Thread(()->{
                log.debug("{}",Utils.getConnection());
            },"t"+(i+1)).start();
        }
    }

    static class Utils {
        private static final ThreadLocal<Connection> tl = new ThreadLocal<>();

        public static Connection getConnection() {
            Connection conn = tl.get();//到当前线程获取资源，每个线程的对象不同
            if (conn == null) {
                conn = innerGetConnection();//创建新的连接对象
                tl.set(conn);//将资源存入当前线程
            }
            return conn;
        }

        private static Connection innerGetConnection() {
            try {
                return DriverManager.getConnection("jdbc:mysql://localhost:3306/my-studydb?useSSL=false", "root", "1230123");
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}

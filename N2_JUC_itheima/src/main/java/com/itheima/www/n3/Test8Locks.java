package com.itheima.www.n3;


import lombok.extern.slf4j.Slf4j;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * 线程八锁
 */
@Slf4j(topic = "c.Test8Locks")
public class Test8Locks {
    public static void main(String[] args) {
        Number n1 = new Number();
        Number n2 = new Number();
        new Thread(() -> {
            log.debug("begin");
            n1.a();
        },"t1").start();
        new Thread(() -> {
            log.debug("begin");
            n1.b();
        },"t2").start();

        new Thread(() -> {
            log.debug("begin");
            n1.c();
        },"t3").start();
    }
}

@Slf4j(topic = "c.Number")
class Number {
    public synchronized void a() {
        sleep(1);
        log.debug("1");
    }

    public synchronized void b() {
        log.debug("2");
    }

    public void c() {
        log.debug("3");
    }
}

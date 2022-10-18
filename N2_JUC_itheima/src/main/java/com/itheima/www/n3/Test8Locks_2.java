package com.itheima.www.n3;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Test8Locks_2 {
    public static void main(String[] args) {
        Number n1 = new Number();
        new Thread(){
            @Override
            public void run() {
                Number.a();
            }
        }.start();

        new Thread(()->n1.b()).start();
    }
    static class Number{
        public static synchronized void a(){
            Sleeper.sleep(1);
            log.debug("1");
        }

        public synchronized void b(){
        log.debug("2");
        }
    }
}

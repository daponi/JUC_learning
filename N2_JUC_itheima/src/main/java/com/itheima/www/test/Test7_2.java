package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * try中 , 报错的那一行代码之后 一直到try结束为止的这一段代码 , 是不会再执行的。
 * 一段代码前有异常抛出，并且这个异常没有被捕获，这段代码将产生编译时错误
 * 异常被try…catch所捕获，若此时catch语句中没有抛出新的异常，则这段代码能够被执行
 * 若在一个条件语句中抛出异常，则程序能被编译，但条件语句后的语句不会被执行。
 *
 * 注意这里不会继续执行的代码是在和sleep一起包在trycatch里的代码，
 * 如果你有代码在run方法里，但是在trycatch块之后的，打断后会继续执行，最后再抛出异常
 */
@Slf4j
public class Test7_2 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(){
            @Override
            public void run() {
                log.debug("enter sleep 2...");
                try {
                    Thread.sleep(2000);
                    log.debug(" try ---------");
                } catch (InterruptedException e) {
                    log.debug("wake up...");
                    e.printStackTrace(); //没有抛出新的异常，则这段代码能够被执行，try-catch外继续执行
                    log.debug(" catch ---------");
                }
                    log.debug(" run ---------");
            }
        };

        t.start();

        log.debug("t state 1 : {}",t.getState());
        log.debug("start interrupting...");
        t.interrupt();
        log.debug("t state 3 : {}",t.getState());

    }
}

package com.itheima.www.pattem;


import lombok.extern.slf4j.Slf4j;

/**
 * 两阶段终止模式(Two Phase Termination)
 * 在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会。
 * 1. 错误思路 :
 * 使用线程对象的 stop() 方法停止线程
 * ● stop 方法会真正杀死线程，如果这时线程锁住了共享资源，那么当它被杀死后就再也没有机会释放锁， 其它线程将永远无法获取锁
 * ● 使用 System.exit(int) 方法停止线程 ,目的仅是停止一个线程，但这种做法会让整个程序都停掉
 */

@Slf4j(topic = "c.TestTwoPhaseTermination")
public class TestTwoPhaseTermination {
    public static void main(String[] args) throws InterruptedException {
        TPTVolatile t = new TPTVolatile();
        t.start();

        Thread.sleep(3500);
        log.debug("stop");
        t.stop();
    }
}
@Slf4j(topic = "c.TPTInterrupt")
class TPTInterrupt {
    private Thread thread;

    public void start(){
        thread = new Thread(() -> {
            while(true) {
                Thread current = Thread.currentThread();
                if(current.isInterrupted()) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("将结果保存");
                } catch (InterruptedException e) {
                    current.interrupt();
                }

            }
        },"监控线程");
        thread.start();
    }

    public void stop() {
        thread.interrupt();
    }
}
@Slf4j(topic = "c.TPTVolatile")
class TPTVolatile {
    private Thread thread;
    private volatile boolean stop = false;

    public void start(){
        thread = new Thread(() -> {
            while(true) {
                Thread current = Thread.currentThread();
                if(stop) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("将结果保存");
                } catch (InterruptedException e) {
                }
            }
        },"监控线程");
        thread.start();
    }

    public void stop() {
        stop = true;
        thread.interrupt();
    }
}



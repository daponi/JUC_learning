package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 终止模式之两阶段终止模式(Two Phase Termination)
 * 在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会
 *
 * 使用可见性volatile
 * 需求: 该事件只需要一个线程执行一次即可
 * 解决：
 * 同步模式之Balking （犹豫）模式用在一个线程发现另一个线程或本线程已经做了某一件相同的事，那么本线程就无需再做了，直接结束返回，多用于web环境下
 */
@Slf4j(topic = "c.TwoPhaseTermination")
public class Test13_3 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination_3 tpt = new TwoPhaseTermination_3();
        // 执行3次但只执行了1次
        tpt.start();
        tpt.start();
        tpt.start();

        /*Thread.sleep(3500);
        log.debug("停止监控");
        tpt.stop();*/
    }
}

@Slf4j(topic = "c.TwoPhaseTermination")
class TwoPhaseTermination_3 {
    // 监控线程
    private Thread monitorThread;
    // 停止标记
    private volatile boolean stop = false;
    // 判断是否执行过 start 方法,Balking （犹豫）模式
    private boolean starting = false;

    // 启动监控线程
    public void start() {
        // Balking （犹豫）模式
        synchronized (this) {
            if (starting) { // false
                return;
            }
            starting = true;
        }
        monitorThread = new Thread(() -> {
            while (true) {
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stop) {
                    log.debug("料理后事");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录");
                } catch (InterruptedException e) {
                }
            }
        }, "monitor");
        monitorThread.start();
    }

    // 停止监控线程
    public void stop() {
        stop = true;
        monitorThread.interrupt();
    }
}

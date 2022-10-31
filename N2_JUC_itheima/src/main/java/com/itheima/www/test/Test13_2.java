package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 终止模式之两阶段终止模式(Two Phase Termination)
 * 在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会
 *
 * 使用可见性volatile
 */
@Slf4j
public class Test13_2 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhaseTermination tpt = new TwoPhaseTermination();
        tpt.start();
        Thread.sleep(3000);
        tpt.stop();
    }

}

@Slf4j
class TwoPhaseTermination{
    //监控线程
    private Thread monitorThread;
    //停止标记
    private volatile boolean stopFlag=false;  //使用volatile关键字保持可见性
    //判断是否执行过start方法
    private boolean startFlag =false;

    // 启动监控线程
    public void start(){
        monitorThread = new Thread(() -> {
            while (true){
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stopFlag ) {
                    log.debug("料理后事。。。");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.debug("执行监控记录。。。");
                } catch (InterruptedException e) {
                }
            }
        },"monitoer");
        monitorThread.start();
    }
        // 停止线程
    public void stop(){
        stopFlag=true;
        log.debug("标记已经改变");
        monitorThread.interrupt();
    }
}

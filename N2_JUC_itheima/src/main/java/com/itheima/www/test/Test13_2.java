package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 终止模式之两阶段终止模式(Two Phase Termination)
 * 在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会
 */
@Slf4j
public class Test13_2 {
    public static void main(String[] args) {

    }

}

@Slf4j
class TwoPhaseTermination{
    //监控线程
    private Thread monitorThread;
    //停止标记
    private volatile boolean stopFlag=false;
    //判断是否执行过start方法
    private boolean startFlag =false;

    // 启动监控线程
    public void start(){
        synchronized (this){
            if (startFlag) {
                return;
            }
            startFlag=true;
        }

        monitorThread = new Thread(() -> {
            while (true){
                Thread current = Thread.currentThread();
                // 是否被打断
                if (stopFlag ) {
                    log.info("料理后事。。。");
                    break;
                }
                try {
                    Thread.sleep(1000);
                    log.info("执行监控记录。。。");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"monitoer");
    }
        // 停止线程
    public void stop(){
        stopFlag=true;
        monitorThread.interrupt();
    }
}

package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

/**
 * 终止模式之两阶段终止模式(Two Phase Termination)
 * 在一个线程 T1 中如何“优雅”终止线程 T2？这里的【优雅】指的是给 T2 一个料理后事的机会
 * bug是如果监控活动是个长业务，那么stop不会立刻停止，而是在这轮执行完到了下一论才停止
 */
@Slf4j
public class Test13 {
    public static void main(String[] args) throws InterruptedException {
        TwoPhase_Termination tpt = new TwoPhase_Termination();
        tpt.start();
        Thread.sleep(4000);
        tpt.stop();
    }
}

@Slf4j
class TwoPhase_Termination{
    //监控线程
    private Thread monitorThread;

    // 启动监控线程
    public void start(){

        monitorThread = new Thread(() -> {
            while (true){
                Thread current = Thread.currentThread();
                // 是否被打断
                if (current.isInterrupted() ) {
                    log.info("料理后事。。。");
                    break;
                }
                try {
                    Thread.sleep(1000);  // 情况1，被打醒isInterrupted=false
                    log.info("执行监控记录。。。");// 情况2，被打断isInterrupted=true
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    // 设置打断标记isInterrupted=true，因为sleep出现异常后会清除打断标记
                    current.interrupt();
                }
            }
        },"monitoer");

        monitorThread.start();
    }

    // 停止线程
    public void stop(){
        monitorThread.interrupt();
    }
}
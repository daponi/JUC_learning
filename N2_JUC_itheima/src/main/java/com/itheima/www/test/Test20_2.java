package com.itheima.www.test;

import com.itheima.www.pattem.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 同步模式之保护性暂停：
 *例子:t1线程需要t2线程计算的值，
 * 因为要等待另一方的结果，因此归类到同步模式，不用join而用wait/notify(all)，因为Join是整个线程结束才返回，但是阻塞的t1线程只需要那个response有值没必要等另一个线程全部执行完
 * 等待线程不需要完全等待处理线程执行完，处理线程可能处理了一半就获取到响应结果从而唤醒等待线程了，不需要像join一样完全等待，
 * 而且线程调用notify后不会立马释放锁，而是需要等代码块执行完，且消费者线程被唤醒了仍需要进入entryList竞争时间片
 *
 * 增加超时功能
 * 该例子就是模仿的Thread.join()的源码
 */
@Slf4j
public class Test20_2 {
    public static void main(String[] args) {
        GuardedObject_2 GuardedObject_2 = new GuardedObject_2();
        new Thread(()->{
            log.debug("等待结果");
             Object result=GuardedObject_2.get(2000); // 超时等待2秒钟
            log.debug("结果：{}",result);
        },"t1").start();

        new Thread(()->{
            log.debug("开始下载...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GuardedObject_2.complete("null"); // null
        },"t2").start();
    }

}

class GuardedObject_2{
    // 保存结果
    private Object response;

    /**
     * 获取结果，增加超时功能
     * @return
     */
    public Object get(long timeout){
        synchronized(this){
            // 开始时间 15:00：00
            long begin= System.currentTimeMillis();
            // 经历的时间
            long passedTime=0;

            // 没有结果则自旋加锁
            while (response==null){
                // 这一轮循环应该等待的时间
                long waitTime=timeout-passedTime;
                if (waitTime<=0){
                    break;
                }
                try {
                    this.wait(waitTime);//wait 会释放锁，但调用它的前提是当前线程占有锁(即代码要在 synchronized 中)。,参数不能是timeout，否则虚假唤醒时又重置了超时时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求经历的时间
                passedTime=System.currentTimeMillis()-begin;// 15:00:02  2s、1s
            }
            return response;
        }
    }

    /**
     * 计算记过
     * @param obj
     */
    public void complete(Object obj){
        synchronized(this){
            // 给结果成员变量赋值
            this.response=obj;
            this.notifyAll();// 唤醒全部waitSet的线程
        }
    }
}
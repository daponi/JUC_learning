package com.itheima.www.test;

import com.itheima.www.pattem.Downloader;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 *  同步模式:保护性暂停(Guarded Suspension)，用在一个线程等待另一个线程的执行结果
 * 要点
 * 1.有一个结果需要从一个线程传递到另一个线程，让他们关联同一个 GuardedObject
 * 2.如果有结果不断从一个线程到另一个线程那么可以使用消息队列（见生产者/消费者）
 * 3.JDK 中，join 的实现、Future 的实现，采用的就是此模式
 *
 *例子:t1线程需要t2线程计算的值，
 * 因为要等待另一方的结果，因此归类到同步模式，不用join而用wait/notify(all)，因为Join是整个线程结束才返回，但是阻塞的t1线程只需要那个response有值没必要等另一个线程全部执行完
 * 等待线程不需要完全等待处理线程执行完，处理线程可能处理了一半就获取到响应结果从而唤醒等待线程了，不需要像join一样完全等待，
 * 而且线程调用notify后不会立马释放锁，而是需要等代码块执行完，且消费者线程被唤醒了仍需要进入entryList竞争时间片
 */
@Slf4j
public class Test20 {
    public static void main(String[] args) {
        GuardedObject guardedObject = new GuardedObject();
        new Thread(()->{
            log.debug("等待结果");
            List<String> list = (List<String>) guardedObject.get();
            log.debug("结果大小：{}",list.size());
        },"t1").start();

        new Thread(()->{
            log.debug("开始下载...");
            try {
                List<String> list =Downloader.download();
                guardedObject.complete(list);
            } catch (IOException e) {
                e.printStackTrace();
            }
        },"t2").start();
    }

}
class GuardedObject{
    // 保存结果
    private Object response;

    /**
     * 获取结果
     * @return
     */
    public Object get(){
        synchronized(this){
            // 没有结果则自旋加锁
            while (response==null){
                try {
                    this.wait();//wait 会释放锁，但调用它的前提是当前线程占有锁(即代码要在 synchronized 中)。
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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
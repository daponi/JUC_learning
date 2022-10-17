package com.itheima.www.test;


import lombok.extern.slf4j.Slf4j;

/**
 * interrupt的使用会将打断标记isInterrupted设置为true，但sleep除外它将打断标记设置为true但sleep会删出打断标记扔为false
 * interrupt具体到底中断还是继续运行，是由被通知的线程自己决定的。
 * stop方法是真正意义上的打断，但是存在缺陷。
 *
 * interrupt() 方法仅仅是在当前线程中打一个停止的标记，并不是真的停止线程。并不会立即终止线程，
 * 而是通知目标线程有人希望你终止。至于目标线程收到通知后会如何处理，则完全由目标线程自行决定。
 */
@Slf4j(topic = "c.Test12")
public class Test12 {

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            while(true) {
                boolean interrupted = Thread.currentThread().isInterrupted();
                if(interrupted) {
                    log.debug("被打断了, 退出循环");
                    break;
                }
            }
        }, "t1");
        t1.start();

        Thread.sleep(1000);
        log.debug("interrupt");
        t1.interrupt();
        log.debug("打断标记：{}",t1.isInterrupted());
    }
}

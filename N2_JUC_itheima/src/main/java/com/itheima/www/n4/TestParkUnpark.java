package com.itheima.www.n4;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static com.itheima.www.n2.util.Sleeper.sleep;

/**
 * 与 Object 的 wait & notify 相比
 * 1.wait，notify 和 notifyAll 必须配合 Object Monitor 一起使用，而 park，unpark 不必
 * 2.park & unpark 是以线程为单位来精确而【阻塞】和【唤醒】线程，而 notify 只能随机唤醒一个等待线程，notifyAll,是唤醒所有等待线程，就不那么【精确】
 * 3.park & unpark 可以先 unpark，而 wait & notify 不能先 notify
 *
 * park 线程, 不会清空打断状态
 * LockSupport.park();用于禁止当前线程进行线程调度，除非许可证(即打断标记)可用即为false。
 * 使用interrupt()打断该线程时，将打断标记设为真即isInterrupted()会返回为true，线程苏醒从park()位置继续执行，
 * 再遇到park()线程不会暂停因为打断标记为true；倘若打断标记再次设为假即isInterrupted()重新设置为false，则再遇到park()线程仍会暂停，可使用 Thread.interrupted()返回打断标记后会清除打断标记，将其设为false。
 *
 */
@Slf4j(topic = "c.TestParkUnpark")
public class TestParkUnpark {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            log.debug("start...");
            sleep(2);
            // 如果许可证可用，则它被消耗，并且该呼叫立即返回; 否则当前线程对于线程调度目的将被禁用，并且处于休眠状态
            LockSupport.park(); // 消耗掉许可证，即该处的park()没有作用，线程不会进入阻塞
            log.debug("park...");
            LockSupport.park(); // 许可证已被消耗，进入waitSet的阻塞状态
            log.debug("park...2");
        }, "t1");
        t1.start();

        sleep(1);
        //为给定的线程提供许可证（如果尚未提供）。 如果线程在park被阻塞，那么它将被解除阻塞。 否则，其下一次拨打park保证不被阻止。 如果给定的线程尚未启动，则此操作无法保证完全没有任何影响。
        LockSupport.unpark(t1); //
        log.debug("unpark...");
        sleep(2);
        LockSupport.unpark(t1);
        log.debug("unpark 2...");
    }
}

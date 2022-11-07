package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 有了自定义同步器，很容易复用AQS，实现一个功能完备的自定义锁(不可重入锁)
 * 继承Lock时要实现它的方法太多，可以使用同步器类AbstractQueuedSynchronizer
 *
 * 这个是仿Reentrantlock的实现，外部类继承Lock，内部类继承AQS
 */
@Slf4j
public class TestAQS {
    public static void main(String[] args) {
        MyLock myLock = new MyLock();
        new Thread(() -> {
            myLock.lock();
            log.debug("lock.1..");
            myLock.lock();
            log.debug("lock.2..");
            try {
                log.debug("successful lock...");
                Sleeper.sleep(2);
            } finally {
                myLock.unlock();
                log.debug("unlock....");
            }
        }, "t1").start();

        new Thread(() -> {
            myLock.lock();
            log.debug("lock...");
            try {
                log.debug("successful lock...");
            } finally {
                myLock.unlock();
                log.debug("unlock....");
            }
        }, "t2").start();

    }
}


// 自定义锁(不可重入锁)
class MyLock implements Lock {
    private MySync mySync = new MySync();

    /**
     * 同步器类，想写独占锁则需要重写它的部分方法
     * 供外部自定义锁的使用
     * state为0时没加锁，为1则加锁
     * AQS的acquire、release方法是aqs的固定机制，比如怎么把线程阻塞，怎么加入队列等等，而tryxx是自己实现的，自己决定是否可重入，是否可共享等等
     */

    class MySync extends AbstractQueuedSynchronizer {

        @Override
        protected boolean tryAcquire(int arg) {
            if (arg == 1) {

                if (compareAndSetState(0, 1)) { // 用CAS保证原子性
                    setExclusiveOwnerThread(Thread.currentThread()); // 设置类的线程拥有者
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (arg == 1) {
                if (getState() == 0) {
                    throw new IllegalMonitorStateException();
                }
                    setExclusiveOwnerThread(null);
                    setState(0);//写屏障，前面修改对其它线程可见写到最后，不用CAS因为已经上锁了资源受到保护了
                    return true;
            }
            return false;
        }

        @Override
        protected boolean isHeldExclusively() {

            // return getState()==1;//虽然我们通常必须先读state再读owner，但我们不需要这样做只要来检查当前线程是否是所有者，看ReentrantLock源码
            return getExclusiveOwnerThread() == Thread.currentThread();
        }

        /**
         * 创建条件变量的方法
         * ConditionObject 是AbstractQueuedSynchronizer的内部类
         */

        public Condition newCondition() {
            return new ConditionObject();
        }
    }


    @Override // 加锁，失败进入队列阻塞
    public void lock() {
        // mySync.tryAcquire(1); // 只尝试加锁1次
        mySync.acquire(1);
    }

    @Override // 加锁，  当前线程进入entryList等待锁进入阻塞状态时，可以被其它线程打断
    public void lockInterruptibly() throws InterruptedException {
        mySync.acquireInterruptibly(1);
    }

    @Override // 尝试加锁，若不成功则返回false，线程不会进入队列阻塞
    public boolean tryLock() {
        return mySync.tryAcquire(1);
    }

    @Override // 尝试加锁，不成功，进入等待队列，有时限
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return mySync.tryAcquireNanos(1, unit.toNanos(time));
    }

    @Override // 解锁
    public void unlock() {
        // mySync.tryRelease(0); //只修改状态和锁拥有者但不会唤醒队列阻塞线程
        mySync.release(0);//同时唤醒队列线程
    }

    @Override // 创建条件变量
    public Condition newCondition() {
        return mySync.newCondition();
    }
}
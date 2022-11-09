package com.itheima.www.n8;

import com.itheima.www.n2.util.Sleeper;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.StampedLock;

/**
 * 该类自JDK 8加入，是为了进一步优化读性能，它的特点是在使用读锁、写锁时都必须配合【戳】使用
 *
 * 读取操作是在验戳之前，担心在读取的时候写线程过来。如果都验戳成功了，说明已经读取完毕了。那么就算戳改了，也用不到了。
 * 乐观读，StampedLock支持tryOptimisticRead方法（乐观读），先读取完毕后需要做一次戳校验如果校验通过，表示这期间确实没有写操作，数据可以安全使用，如果校验没通过，需要重新获取读锁，保证数据安全。
 */

@Slf4j
public class TestStampedLock {
    public static void main(String[] args) {
        DataContainerStamped data = new DataContainerStamped(100);
        new Thread(()->{
            // data.read(1);
            data.write(200);

        },"t1").start();
        Sleeper.sleep(0.5);

        new Thread(()->{
            data.read(1);
            // data.write(200);
        },"t2").start();
    }
}

@Slf4j
class DataContainerStamped {
    private int data;
    private StampedLock lock = new StampedLock();

    public DataContainerStamped(int data) {
        this.data = data;
    }

    public int read(int readTime) {
        long stamp = lock.tryOptimisticRead();
        log.debug("optimistic read locking...{}", stamp);
        Sleeper.sleep(readTime);
        // 乐观读
        if (lock.validate(stamp)) {
            log.debug("read finish...stamp:{}, data:{}", stamp, data);
            return data;
        }

        // 锁升级-读锁 ,需要再次获取时间戳，不然会报.IllegalMonitorStateException
            log.debug("updating to read lock ...{}", stamp);
        try {
            stamp = lock.readLock();
            log.debug("read lock {}", stamp);
            Sleeper.sleep(readTime);
            log.debug("read finish...stamp:{}, data:{}", stamp, data);
            return data;
        } finally {
            log.debug("read unlock {}", stamp);
            lock.unlockRead(stamp);
        }


    }

    public void write(int newData){
        long stamp = lock.writeLock();
        try {
            log.debug("write lock {} ",stamp);
            Sleeper.sleep(2);
            this.data=newData;
        } finally {
            log.debug("write unlock {}", stamp);
            lock.unlockWrite(stamp);

        }

    }
}

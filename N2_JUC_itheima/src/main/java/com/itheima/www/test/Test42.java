package com.itheima.www.test;

import com.itheima.www.n4.UnsafeAccessor;
import sun.misc.Unsafe;

/**
 * 使用Unsafe自定义 AtomicData 实现之前线程安全的AtomicInteger原子整数对Account实现
 * 加深对Unsafe的使用
 * 为了方便测试，基层了自定义的Account, Account就相当于业务类，主要还是看自定义的AtomicInteger
 */
public class Test42 {
    public static void main(String[] args) {
        Account myAtomicInteger = new MyAtomicInteger(10000);
        Account.demo(myAtomicInteger);
    }
}


/**
 * 使用Unsafe自定义的AtomicInteger
 * 为了方便测试，基层了自定义的Account, Account就相当于业务类，主要还是看自定义的AtomicInteger
 */
class MyAtomicInteger implements Account{
    private volatile int value;
    private static final long valueOffest;
    private static final Unsafe UNSAFE;

    static {
        UNSAFE = UnsafeAccessor.getUnsafe();// 从自己写的工具类反射获得unsafe对象
        try {
            valueOffest = UNSAFE.objectFieldOffset(MyAtomicInteger.class.getDeclaredField("value"));
        } catch (NoSuchFieldException e) {
            // 静态代码块没办法把异常再往外抛，因为它是检查异常，可以转换为运行时异常抛出去或直接抛错误Error
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public int getValue() {
        return value;
    }

    public MyAtomicInteger(int value) {
        this.value = value;
    }

    //CAS
    public void decrement(int amount){
        while (true) {
            // 获取共享变量旧值，可以在这一行加入断点，修改 data 调试来加深理解
            int prev=this.value;
            int next=prev-amount;
            // cas 尝试修改 data 为 旧值 + amount，如果期间旧值被别的线程改了，返回 false
            if (UNSAFE.compareAndSwapInt(this, valueOffest, prev, next)) {
                break;
            }
        }

    }

    @Override
    public Integer getBalance() {
        return getValue();
    }

    @Override
    public void withDraw(Integer amount) {
        decrement(amount);
    }
}
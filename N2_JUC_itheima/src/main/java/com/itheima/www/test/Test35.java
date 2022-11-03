package com.itheima.www.test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 6.4 原子引用
 * 为什么需要原子引用类型？
 * AtomicReference
 * AtomicMarkableReference
 * AtomicStampedReference
 *
 * BIgdecimal这里也是一个面试题：Bigdecimal一定能保证数据正确？不能，它不能保证原子性
 */
public class Test35 {
    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("10000");
        DecimalAccount cas = new DecimalAccountCas(bigDecimal);
        DecimalAccount.demo(cas);
    }
}

class DecimalAccountCas implements DecimalAccount{
    private AtomicReference<BigDecimal> balance;

    public DecimalAccountCas(BigDecimal balance) {
        // this.balance = balance;
        this.balance=new AtomicReference<>(balance);
    }

    @Override
    public BigDecimal getBalance() {
        return this.balance.get();
    }

    @Override
    public void withDraw(BigDecimal amount) {
        while (true) {
            BigDecimal prev = this.balance.get();
            BigDecimal next = prev.subtract(amount);
            if (balance.compareAndSet(prev, next)) {
                return;
            }
        }
    }
}


/**
 * 接口的成员变量的默认修饰符为：public static final
 * 接口的方法的默认修饰符是：public abstract
 */
interface DecimalAccount {
    // 获取余额
    BigDecimal getBalance();

    // 取款
    void withDraw(BigDecimal amount);


    /**
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(DecimalAccount account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withDraw(BigDecimal.TEN); // 10
            }));
        }

        long start = System.nanoTime();
        // 启动全部线程
        ts.forEach(Thread::start);
        ts.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        long end = System.nanoTime();
        System.out.println("balance:" + account.getBalance() + ",cost time:" + (end - start) / 1000_000 + "ms");


    }
}

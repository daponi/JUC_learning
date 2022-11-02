package com.itheima.www.test;


import jdk.internal.org.objectweb.asm.tree.analysis.Value;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TestAccount {
    public static void main(String[] args) {
        Account account = new AccountUnsafe(10000);
        Account.demo(account);

        Account cas=new AccountCas(10000);
        Account.demo(cas);
    }
}

class AccountCas implements Account{
    private AtomicInteger balance;

    public AccountCas(int balance) {
        this.balance=new AtomicInteger(balance);
    }

    @Override
    public Integer getBalance() {
        return balance.get();
    }

    @Override
    public void withDraw(Integer amount) {
        // 需要不断尝试，直到成功为止
/*        while (true) {
            int prev = balance.get();//如1000
            int next = prev-amount;//1000-10=990
            *//**
             * compareAndSet 正是做这个检查，在 set 前，先比较 prev 与当前值
             * * - 不一致了，next 作废，返回 false 表示失败
             * 比如，别的线程已经做了减法，当前值已经被减成了 990，那么本线程的这次 990 就作废了，进入 while 下次循环重试
             * - 一致，以 next 设置为新值，返回 true 表示成功
             *//*
            if (balance.compareAndSet(prev, next)) {
               break;
            }
        }*/

        // 原子操作
        // balance.getAndAdd(-1*amount);
        balance.getAndUpdate(Value->Value-amount); //源码就是CAS

    }
}


/**
 * 不对balance加锁时是不安全的
 * 读写操作时，必须给共享资源balance加锁
 */
class AccountUnsafe implements Account {
    private Integer balance;

    public AccountUnsafe(Integer balance) {
        this.balance = balance;
    }

    //老师这个加锁确实是多余了,首先可见性在这里不存在,这个方法是等所有子线程结束后由主线程调用的,子线程结束肯定会把数据写回主内存
    // 但实际开发中多线程并发取款时应该也要加锁，防止脏读
    @Override
    public Integer getBalance() {
        synchronized (this) {
            return this.balance;
        }
    }

    @Override
    public void withDraw(Integer amount) {
        synchronized (this) {
            this.balance -= amount;
        }
    }
}


/**
 * 接口的成员变量的默认修饰符为：public static final
 * 接口的方法的默认修饰符是：public abstract
 */
interface Account {
    // 获取余额
    Integer getBalance();

    // 取款
    void withDraw(Integer amount);


    /**
     * 测试方法：
     * 方法内会启动 1000 个线程，每个线程做 -10 元 的操作
     * 如果初始余额为 10000 那么正确的结果应当是 0
     */
    static void demo(Account account) {
        List<Thread> ts = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            ts.add(new Thread(() -> {
                account.withDraw(10);
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

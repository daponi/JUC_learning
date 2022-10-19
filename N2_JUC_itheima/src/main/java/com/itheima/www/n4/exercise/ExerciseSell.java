package com.itheima.www.n4.exercise;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * 多线程的卖票练习
 * 临界区，需要加synchronized否则会有线程安全问题
 * 单独测试不好出问题，windows系统可用cmd脚本多测试几次
 */
@Slf4j
public class ExerciseSell {
    public static void main(String[] args) throws InterruptedException {
        // 模拟多人买票
        TicketWindow window = new TicketWindow(1000);
        // 统计卖出的票数
        List<Integer> amountList=new Vector<>();
        // 所有线程的集合
        List<Thread> threadList=new ArrayList<>();
        // 多线程即多个人买票
        for (int i = 0; i < 20000; i++) {
            Thread thread = new Thread(()->{
                // 买票
                int amount=window.sell(randomAmount()); //多线程访问共享资源
                amountList.add(amount); // 原子操作
            });
            threadList.add(thread);
            thread.start();
        }

        // 主线程等待全部人买完
        for (Thread thread : threadList) {
            thread.join();
        }

        // 统计卖出的票数和剩余的票数
        log.debug("剩余的票数:{}",window.getCount());
        log.debug("卖出的票数:{}",amountList.stream().mapToInt(i->i).sum());
        // System.out.println("剩余的票数:{}"+window.getCount());
        // System.out.println("卖出的票数:{}"+amountList.stream().mapToInt(i->i).sum());
    }

    // Random是线程安全的
    static Random random=new Random();
    // 随机买的票数1-5
    public static int randomAmount(){
        return random.nextInt(5)+1;
    }
}

// 售卖窗口
class TicketWindow {
    private int count; // 存在线程安全问题

    public TicketWindow(int count) {
        this.count = count;
    }

    // 获取票的数量
    public int getCount() {
        return count;
    }

    // 售票,这是临界区，需要加synchronized否则会有线程安全问题
    public synchronized int sell(int amount) {
        if (this.count >= amount) {
            this.count -= amount;
            return amount;
        } else {
            return 0;
        }
    }

}

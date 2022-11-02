package com.itheima.www.test;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * 累加器性能比较
 * 原子累加器，jak8后单独出了累加器，性能比cas更快，它们都是Striped64的是实现类
 *
 * 性能提升的原因很简单，就是在CAS的while里有线程竞争时，设置多个累加单元，Therad-0 累加 Cell[0]，而 Thread-1 累加
 * Cell[1]... 最后将结果汇总。这样它们在累加时操作的不同的 Cell 变量，因此减少了 CAS 重试失败，从而提高性能
 */
public class Test41 {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            demo(() -> new AtomicLong(0), (adder) -> adder.getAndIncrement());
        }

        for (int i = 0; i < 5; i++) {
            demo(() -> new LongAdder(), (adder) -> adder.increment());
        }
    }

    /**
     * 4个线程里每个都自增50万次
     *
     * @param adderSupplier () -> 结果    提供累加器对象
     * @param action        (参数) ->     执行累加操作
     * @param <T>
     */
    private static <T> void demo(Supplier<T> adderSupplier, Consumer<T> action) {
        T adder = adderSupplier.get();
        ArrayList<Thread> ts = new ArrayList<>();
        // 4 个线程，每人累加 50 万
        for (int i = 0; i < 4; i++) {
            ts.add(new Thread(() -> {
                for (int j = 0; j < 500000; j++) {
                    action.accept(adder);
                }
            }));
        }
        long start = System.nanoTime();
        ts.forEach(thread -> thread.start());
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.nanoTime();
        System.out.println(adder + ",cost:" + (end - start) / 1000_000 +"ms");
    }
}

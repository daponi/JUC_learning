package com.itheima.www.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 原子数组: 保护数组里的元素
 * AtomicIntegerArray
 * AtomicLongArray
 * AtomicReferenceArray
 *
 * 测试普通数组或原子数组的安全性的方法
 */
public class Test39 {
    public static void main(String[] args) {
        // 不是线程安全的，结果不全为10000
        demo(()->new int[10],
                (array)->array.length,
                (array,index)->array[index]++,
                (array)-> System.out.println(Arrays.toString(array)));

        // 原子整型数组
        demo(()->new AtomicIntegerArray(10),
                (arrays)-> arrays.length(),
                (array,index)->array.getAndIncrement(index),
                (array)-> System.out.println(array));
    }

    /**
     * 测试普通数组或原子数组的安全性
     *
     * 参数1，提供数组、可以是线程不安全数组或线程安全数组
     * 参数2，获取数组长度的方法
     * 参数3，自增方法，回传 array, index
     * 参数4，打印数组的方法
     *  supplier 提供者 无中生有  ()->结果
     *  function 函数   一个参数一个结果   (参数)->结果  ,  BiFunction (参数1,参数2)->结果
     *  consumer 消费者 一个参数没结果  (参数)->void,      BiConsumer (参数1,参数2)->void
     *  <T> T表示返回值是一个泛型，传递啥，就返回啥类型的数据，而单独的T就是表示限制你传递的参数类型
     */
    private static <T> void demo(
            Supplier<T> arraySupplier,
            Function<T,Integer> lengthFun,
            BiConsumer<T,Integer> putConsumer,
            Consumer<T> printConsumer) {
        ArrayList<Thread> ts = new ArrayList<>();
        T array = arraySupplier.get();
        int length = lengthFun.apply(array);

        for (int i = 0; i < length; i++) {
            // 每个线程对数组作10000次操作
            ts.add(new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    /**
                     *   j     length    j%length
                     *   0      10         0
                     *   1      10         1
                     *   2      10         2
                     *  ...     ...        ...
                     *  只得到个位数
                     */
                    putConsumer.accept(array, j%length); // j%length:元素下标

                }
            }));
        }
        ts.forEach(Thread::start); //启动所有线程
        ts.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });// 等待所有线程启动
        printConsumer.accept(array);
    }
}

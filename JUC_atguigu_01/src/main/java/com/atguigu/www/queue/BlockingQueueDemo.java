package com.atguigu.www.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * BlockingQueue的演示Demo
 */

//阻塞队列
public class BlockingQueueDemo {

    public static void main(String[] args) throws InterruptedException {
        //创建一个定长为3的数组阻塞队列
        BlockingQueue<String> blockingQueue = new ArrayBlockingQueue<>(3);

        //第一组，add()、remove
       // System.out.println(blockingQueue.add("a"));//队列加入元素a
       // System.out.println(blockingQueue.add("b"));
       // System.out.println(blockingQueue.add("c"));
       // System.out.println(blockingQueue.element());//返回队列的头部元素,
       // // System.out.println(blockingQueue.add("d"));// add()如果当前没有可用空间，则抛出IllegalStateException。
       //
       // System.out.println(blockingQueue.remove());// 返回并删除此队列的头元素
       // System.out.println(blockingQueue.remove());
       // System.out.println(blockingQueue.remove());
       // System.out.println(blockingQueue.remove());// 在队列没有元素时继续remove()则抛出NoSuchElementException异常

        /*===================分割线===================*/

        //第二组 offer()、poll()
       // System.out.println(blockingQueue.offer("a"));// offer()将指定的元素插入到此队列中,若队列已满则返回false
       // System.out.println(blockingQueue.offer("b"));
       // System.out.println(blockingQueue.offer("c"));
       // System.out.println(blockingQueue.offer("www"));// offer()若队列已满则返回false
       //
       // System.out.println(blockingQueue.poll()); // poll()返回并删除此队列的头，如果此队列为空，则返回 null
       // System.out.println(blockingQueue.poll());
       // System.out.println(blockingQueue.poll());
       // System.out.println(blockingQueue.poll());// poll()如果此队列为空，则返回 null

        /*===================分割线===================*/

        //第三组 put()、take()
       // blockingQueue.put("a");// BlockingQueue.put()将指定的元素插入到此队列中，等待空格可用。如队列已满则阻塞线程直到队列空闲
       // blockingQueue.put("b");
       // blockingQueue.put("c");
       // // blockingQueue.put("w");// BlockingQueue.put()如队列已满则阻塞线程直到队列空闲。
       //
       // System.out.println(blockingQueue.take()); // take()检索并删除此队列的头，若队列已空则阻塞线程，等待元素可用。
       // System.out.println(blockingQueue.take());
       // System.out.println(blockingQueue.take());
       // System.out.println(blockingQueue.take()); // take()若队列已空则阻塞线程，等待元素可用。

        /*===================分割线===================*/

        //第四组 offer()、poll()加上超时时长
        System.out.println(blockingQueue.offer("a"));
        System.out.println(blockingQueue.offer("b"));
        System.out.println(blockingQueue.offer("c"));
        System.out.println(blockingQueue.offer("w",3L, TimeUnit.SECONDS));

        System.out.println("开始取元素。。。");
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll());
        System.out.println(blockingQueue.poll(3l, TimeUnit.SECONDS));
        System.out.println("取完元素。。。");
        /*===================分割线===================*/
    }
}

package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedList;

/**
 * 异步模式之生产者/消费者
 * 1. 定义
 * 要点
 * 1.与前面的保护性暂停中的 GuardObject 不同，不需要产生结果和消费结果的线程一一对应
 * 2.消费队列可以用来平衡生产和消费的线程资源
 * 3.生产者仅负责产生结果数据，不关心数据该如何处理，而消费者专心处理结果数据
 * 4.消息队列是有容量限制的，满时不会再加入数据，空时不会再消耗数据
 * JDK 中各种阻塞队列，采用的就是这种模式
 *
 * 这里模拟的是线程间的生产/消费模式，Rocket/RabbitMQ是可以实现进程间的生产/消费
 * 把两个条件队列分开会比较好一点。如果阻塞队列的容量很小，并发又高就会有很多无效唤醒
 */
@Slf4j
public class Test21 {
    public static void main(String[] args) {
        MessageQueue messageQueue = new MessageQueue(2);
        // 生产线程
        for (int i = 0; i < 3; i++) {
            int id = i;//Lambda要求的局部变量是final不可变，新键一个局部变量i代替解决
            new Thread(() -> {
                messageQueue.put(new Message(id, "value" + id));
            }, "生产者" + i).start();
        }

        // 1 个消费者线程, 处理结果
        new Thread(() -> {
            while (true) {
                Message message = messageQueue.take();
            }
        }, "消费者").start();

    }
}

// 模拟有容量的双向队列
@Slf4j(topic = "C.MessageQueue")
class MessageQueue {
    // 双向队列
    private LinkedList<Message> list = new LinkedList<>();
    // 队列容量
    private int capacity;


    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    // 消费消息
    public Message take() {
        // 检查队列是否为空
        synchronized (list) {
            while (list.isEmpty()) {
                try {
                    log.debug("队列为空，消费者线程等待......");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // Message first = list.getFirst();// 只返回了消息但是没有从队列中删除
            Message message = list.removeFirst();
            log.debug("已消费消息:{}", message);
            // 唤醒其它线程进行生产
            list.notifyAll();
            return message;
        }
    }

    // 生产消息
    public void put(Message message) {
        synchronized (list) {
            // 检查队列是否已满
            while (list.size() == capacity) {
                try {
                    log.debug("队列已满，生产者线程等待......");
                    list.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            // 将队列加入队列尾部
            list.addLast(message);
            log.debug("已生产消息:{}", message);
            // 唤醒线程进行消费
            list.notifyAll();
        }
    }
}

/**
 * 只有构造方法设立属性，防止线程安全问题，且再加入final防止被子类继承改变方法
 * 只有get方法则不能改变内部属性，
 */
final class Message {
    private int id;
    private Object value;

    public Message(int id, Object value) {
        this.id = id;
        this.value = value;
    }

    public int getId() {
        return id;
    }

    public Object getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}

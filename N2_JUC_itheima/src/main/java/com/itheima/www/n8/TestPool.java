package com.itheima.www.n8;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义实现一个线程池
 *
 * 策略者模式：把具体的操作抽象为一个接口，具体的实现由调用者传进来
 */
@Slf4j
public class TestPool {
    public static void main(String[] args) {
        //实例化对象
        ThreadPool threadPool = new ThreadPool(1, 1000, TimeUnit.MILLISECONDS, 1,
                (queue,task)->{
                    // 若没有任务可执行后，则线程会怎么样？
                    //1)死等
                    // queue.put(task);
                    //2)带超时等待
                    // taskQueue.offer(task, 1500, timeUnit);
                    //3)让调用者放弃任务执行 ,放弃任务2、3
                    // log.debug("放弃{}",task);
                    //4)让调用者抛出异常,是主线程抛出异常，则for到i=1后任务2、3的代码不会执行
                    // throw new RuntimeException("任务执行失败"+task);
                    //5)让调用者自己执行任务,主线程调用的则阻塞队列满时由主线程自己执行，线程池的对象继续执行阻塞队列的任务
                    task.run();
        });

        //测试方法
        for (int i = 0; i < 4; i++) {// 模拟任务数
            int j=i;
            threadPool.execute(() ->{
                try {
                log.debug("完成任务 {}",j);
                    Thread.sleep(1000);//睡眠，模拟执行时间很长
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } );
        }
    }
}

//线程池
@Slf4j
class ThreadPool{
    // 1.任务队列，传任务的所以参数传Runnable
    private BlockingQueue<Runnable> taskQueue;
    // 2.线程集合，正在运行的线程数
    private HashSet<Worker> workers=new HashSet();
    // 3.核心线程数
    private int coreSize;
    // 4.获取任务的超时时间，若超时了则线程关闭
    private long timeout;
    // 超时的时间单位
    private TimeUnit timeUnit;

    // 拒绝策略
    private RejectPolicy<Runnable> rejectPolicy;

    public ThreadPool( int coreSize, long timeout, TimeUnit timeUnit,int queueCapacity,RejectPolicy<Runnable> rejectPolicy) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new BlockingQueue<>(queueCapacity);
        this.rejectPolicy=rejectPolicy;

    }

    //执行任务
    public void execute(Runnable task){
        //1. 当任务数没有超过核心线程数coreSize时，直接将给核心线程Worker对象执行
        synchronized (workers) {
            if (workers.size()<coreSize) {
                Worker worker = new Worker(task);
                log.debug("新增 worker对象：{}, 任务：{}", worker, task);
                workers.add(worker); //记录线程池已有的线程
                worker.start(); //线程启动
            }else {
            //2. 当任务数超过核心线程数coreSize时，将新加入任务队列暂存
                // 若没有任务可执行后，则线程会怎么样？
                //1)死等
                // taskQueue.put(task);
                //2)带超时等待
                // taskQueue.offer(task, timeout, timeUnit);
                //3)让调用者放弃任务执行
                //4)让调用者抛出异常
                //5)让调用者自己执行任务
                /**
                 * 策略模式：把具体的操作抽象为一个接口，具体的实现由调用者传进来
                 * 因为taskQueue持有锁，为了线程安全将在阻塞队列已满时，生产者的操作封装到taskQueue内
                 */
                taskQueue.tryPut(rejectPolicy,task);
            }
        }
    }

    // 执行任务的线程
    class Worker extends Thread {
        private Runnable task;

        public Worker(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            //执行任务，执行完当前任务后还要继续去执行阻塞队列的任务，则需要用while进行循环判断
                //1.worker是刚创建的新task时，传的task不为空，执行任务
                //2.当task执行完毕，继续从任务队列taskQueue执行任务并执行,用while
             // while (task!=null|| (task=taskQueue.take())!=null) {
             while (task!=null|| (task=taskQueue.poll(timeout,timeUnit))!=null) {
                 try {
                     log.debug("正在执行任务...{}", task);
                     task.run();
                 } catch (Exception e) {
                     e.printStackTrace();
                 } finally {
                     task=null;
                 }
             }

             // 该线程执行完当前任务且任务队列也没有任务后，从工作的线程集合中删去
            synchronized (workers) {
                log.debug("worker 对象被移除{}", this);
                workers.remove(this);
            }
        }
    }
}

//策略模式，配合BlockingQueue将接口定义为泛型，更灵动
@FunctionalInterface
interface RejectPolicy<T>{
    void reject(BlockingQueue<T> queue,T task);
}


// 阻塞队列
@Slf4j
class BlockingQueue<T> {
    //1.任务队列 ，双向队列
    private Deque<T> queue = new ArrayDeque<>();
    //2.锁，用于队列和队尾，使用ReentrantLock
    private ReentrantLock lock = new ReentrantLock();
    //3.生产者条件变量
    private Condition fullWaitSet = lock.newCondition();
    //4.消费者条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //5.容量
    private int capacity;
    private long nanos;

    // 带超时的阻塞获取
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try {
            // 将timeout统一转换为纳秒，
            long nanos= unit.toNanos(timeout);
            while (queue.isEmpty()) { //不能用if，唤醒后多判断一次，防止虚假唤醒
                try {
                    if (nanos<=0){
                        return null;
                    }
                    // emptyWaitSet.await(timeout,unit); // 只返回true/false
                    nanos = emptyWaitSet.awaitNanos(timeout); // 被唤醒后，返回超时里剩余的时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    //阻塞获取
    public T take() {
        lock.lock();
        try {
            while (queue.isEmpty()) { //不能用if，唤醒后多判断一次，防止虚假唤醒
                try {
                    emptyWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        } finally {
            lock.unlock();
        }
    }

    //带超时时间的阻塞添加任务
    public boolean offer(T task,long timeout,TimeUnit timeUnit){
        lock.lock();
        try {
            long nanos=timeUnit.toNanos(timeout);
            while (queue.size() == capacity) {//不能用if，唤醒后多判断一次，防止虚假唤醒
                try {
                    if (nanos<=0) {
                        return false;
                    }
                    // fullWaitSet.await();
                    log.debug("等待加入任务队列 {} ...", task);
                    this.nanos = fullWaitSet.awaitNanos(nanos);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.debug("加入任务队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
            return true;
        } finally {
            lock.unlock();
        }
    };

    //阻塞添加任务
    public void put(T task) {
        lock.lock();
        try {
            // 阻塞队列已满不能再添加任务
            while (queue.size() == capacity) {//不能用if，唤醒后多判断一次，防止虚假唤醒
                try {
                    log.debug("等待加入任务队列 {} ...", task);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            log.debug("加入任务阻塞队列 {}", task);
            queue.addLast(task);
            emptyWaitSet.signal();
        } finally {
            lock.unlock();
        }
    }

    // 添加任务，使用拒绝策略，用了策略模式
    public void tryPut(RejectPolicy<T> rejectPolicy,T task){
        lock.lock();
        try {
            // 阻塞队列已满不能再添加任务
            if (queue.size()==capacity) {
                rejectPolicy.reject(this, task);
            }else{ //任务队列没满，则加入阻塞队列
                log.debug("加入任务阻塞队列 {}", task);
                queue.addLast(task);
                emptyWaitSet.signal();
            }
        }finally {
            lock.unlock();
        }

    }

    // 获得容量
    public int size() {
        lock.lock();
        try {
            return capacity;
        } finally {
            lock.unlock();
        }
    }

    public BlockingQueue(int capacity) {
        this.capacity = capacity;
    }
}
package com.itheima.www.test;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 同步模式之保护性暂停：
 * 图中 Futures 就好比居民楼一层的信箱（每个信箱有房间编号），左侧的 t0，t2，t4 就好比等待邮件的居民，右
 * 侧的 t1，t3，t5 就好比邮递员。如果需要在多个类之间使用 GuardedObject 对象，作为参数传递不是很方便，因此设计一个用来解耦的中间类，
 * 这样不仅能够解耦【结果等待者】和【结果生产者】，还能够同时支持多个任务的管理
 *
 * 缺点：1封信就需要1个邮差
 */
@Slf4j
public class Test20_3 {
    public static void main(String[] args) throws InterruptedException {
        // 人开始收信
        for (int i = 1; i <=3; i++) {
            new People().start();
        }
        Thread.sleep(2000);
        // 邮差开始放信
        for (Integer id : MailBoxes.getIds()) {
            new Postman(id, "内容"+id).start();
        }
    }

}

// 业务相关类
@Slf4j(topic = "C.People")
class People extends Thread{
    @Override
    public void run() {
        // 收信
        GuardedObject_3 guardedObject = MailBoxes.createGuardedObject();
        log.debug("收信信格 id: {}",guardedObject.getId());
        Object mail = guardedObject.get(5000);// 只收集5秒
        log.debug("信格收到信件 id:{} , 内容:{}",guardedObject.getId(),mail);
    }
}

// 邮差
@Slf4j(topic = "C.Postman")
class Postman extends Thread{
    // 信件信息
    private int id;
    private String mail;

    public Postman(int id, String mail) {
        this.id = id;
        this.mail = mail;
    }

    @Override
    public void run() {
        // 指定信箱的格子id
        GuardedObject_3 guardedObject = MailBoxes.getGuardedObject(id);
        log.debug("送信 id:{},内容:{}",id,mail);
        // 放到信箱
        guardedObject.complete(mail);
    }
}


// 中间解耦类
class MailBoxes{
    // 保持所有的guardObject,线程安全类hashtable
    private static Map<Integer,GuardedObject_3> boxes=new Hashtable<>();
    private static int id=1;

    //产生唯一id
    private static synchronized int generateId(){
        return id++;
    }

    public static GuardedObject_3 createGuardedObject(){
        GuardedObject_3 go = new GuardedObject_3(generateId());
        boxes.put(go.getId(),go );
        return go;
    }

    public static GuardedObject_3 getGuardedObject(int id){
        return boxes.remove(id); //取出信后要删除，不然会内存泄漏
    }

    public static Set<Integer> getIds() {
        return boxes.keySet();
    }
}



class GuardedObject_3{
    //新增 id 用来标识 Guarded Object
    private int id ;
    // 保存结果
    private Object response;

    public GuardedObject_3(int id) {
        this.id = id;
    }

    // 只有get方法且不能更改，只能初次构造方法时设id，避免发生线程安全问题
    public int getId() {
        return id;
    }

    /**
     * 获取结果，增加超时功能
     * @return
     */
    public Object get(long timeout){
        synchronized(this){
            // 开始时间 15:00：00
            long begin= System.currentTimeMillis();
            // 经历的时间
            long passedTime=0;

            // 没有结果则自旋加锁
            while (response==null){
                // 这一轮循环应该等待的时间
                long waitTime=timeout-passedTime;
                if (waitTime<=0){
                    break;
                }
                try {
                    this.wait(waitTime);//wait 会释放锁，但调用它的前提是当前线程占有锁(即代码要在 synchronized 中)。,参数不能是timeout，否则虚假唤醒时又重置了超时时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 求经历的时间
                passedTime=System.currentTimeMillis()-begin;// 15:00:02  2s、1s
            }
            return response;
        }
    }

    /**
     * 计算记过
     * @param obj
     */
    public void complete(Object obj){
        synchronized(this){
            // 给结果成员变量赋值
            this.response=obj;
            this.notifyAll();// 唤醒全部waitSet的线程
        }
    }
}
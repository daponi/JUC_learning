package com.atguigu.www.sync;

import java.util.concurrent.TimeUnit;



/**
 * 多线程锁 8锁
 *
1 标准访问，创建两个线程分别调用一个对象的sendSMS()、sendEmail()方法，最后是先打印短信还是邮件？
------sendSMS
------sendEmail

2 停4秒在短信方法内，创建两个线程分别调用一个对象的sendSMS()、sendEmail()方法，先打印短信还是邮件？
------sendSMS
------sendEmail

3 新增普通的hello 方法，标准访问，创建两个线程分别调用一个对象的sendSMS()、sendEmail()方法，，先打印短信还是邮件？
------getHello
------sendSMS

4 创建两个线程分别调用两个个对象的sendSMS()、sendEmail()方法，，先打印短信还是邮件？
------sendEmail
------sendSMS

5 两个静态同步方法，1部手机，先打印短信还是邮件
------sendSMS
------sendEmail

6 两个静态同步方法，2部手机，先打印短信还是邮件
------sendSMS
------sendEmail

7 1个静态同步方法,1个普通同步方法，1部手机，先打印短信还是邮件
------sendEmail
------sendSMS

8 1个静态同步方法,1个普通同步方法，2部手机，先打印短信还是邮件
------sendEmail
------sendSMS

 */

class Phone {

    public static synchronized void sendSMS() throws Exception {
        //停留4秒
        // TimeUnit.SECONDS.sleep(4);
        System.out.println("------sendSMS");
    }

    public synchronized void sendEmail() throws Exception {
        System.out.println("------sendEmail");
    }

    public void getHello() {
        System.out.println("------getHello");
    }
}

public class Lock_8 {
    public static void main(String[] args) throws Exception {

        Phone phone = new Phone();
        Phone phone2 = new Phone();

        new Thread(() -> {
            try {
                phone.sendSMS();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "AA").start();

        // 停留让线程创建的效果更明显
        Thread.sleep(100);

        new Thread(() -> {
            try {
               // phone.sendEmail();
               // phone.getHello();
                phone2.sendEmail();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, "BB").start();
    }
}

package com.atguigu.www;

@SuppressWarnings({"all"})
public class Example01 {
    /**
     * 守护线程的验证,用户线程全部结束后则守护线程结束
     * @param args
     */
    public static void main(String[] args) {
        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().isDaemon());
            while (true){
                //保持用户线程一直存在而不结束
            }
        },"Thread01");
        //设置守护线程
        // thread.setDaemon(true);
        thread.start();

        System.out.println(Thread.currentThread().getName()+"\t : over");


    }
}

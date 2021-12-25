package com.atguigu.www;

import org.junit.Test;

public class Test01 {
    @Test
    public void test01(){
        Thread thread = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().isDaemon());
            while (true){
                //保持用户线程一直存在而不结束
            }
        },"Thread01");
        thread.start();

        System.out.println(Thread.currentThread().getName()+"\t : over");
    }

    @Test
    public void test02(){
        /**
         * jvm的类加载机制是自上而下加载，自下而上检查。
         * 开始是由BootStrap ClassLoader加载rt.jar下的文件，也就是java最最核心的部分即java.*；
         * 然后是由Extension ClassLoader加载ext下的文件即javax.*；再有AppClassLoader加载用户自己的文件。
         * 由于BootStrap ClassLoader是用c++写的，所以在返回该ClassLoader时会返回null。
         * 显然，Class为java.lang.Class，是rt.jar中的，由BootStrap ClassLoader加载，所以返回null
         */
        Object o = new Object();
        System.out.println(o.getClass().getClassLoader());//null
        Integer integer = new Integer(4);
        System.out.println(integer.getClass().getClassLoader());//null
        // System.out.println(integer.getClass().getClassLoader().getParent());//ootStrap ClassLoader没有parent则返回NullPointerException

        /**
         * 自定义的类型的ClassLoader是AppClassLoader
         * App ClassLoader的getParent()是Extension ClassLoader
         * Extension ClassLoader的getParent()是BootStrap ClassLoader即返回null
         */
        Test01 test01 = new Test01();
        System.out.println(test01.getClass().getClassLoader());//sun.misc.Launcher$AppClassLoader@18b4aac2
        System.out.println(test01.getClass().getClassLoader().getParent());//sun.misc.Launcher$ExtClassLoader@28a418fc
        System.out.println(test01.getClass().getClassLoader().getParent().getParent());//null
    }

    public static void main(String[] args) {

    }
}


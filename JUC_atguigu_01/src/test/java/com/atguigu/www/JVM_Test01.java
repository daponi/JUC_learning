package com.atguigu.www;

import org.junit.Test;
import org.junit.validator.PublicClassValidator;
import sun.misc.VM;

import javax.swing.plaf.PanelUI;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class JVM_Test01 {
    @Test
    public void test01() {
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

    @Test
    public void test02() {
        /**
         * Thread线程只能Start()用一次
         * native void start0()只声明没有java实现
         * native代表着用的是非java函数库
         */
        Thread thread = new Thread(() -> {
        }, "Thread01");
        thread.start();
        thread.start();//内部使用native void start0()，调用的底层第三方非java函数库

    }

    /**
     * 查看JVM配置参数
     */
    @Test
    public void test03() {
        System.out.println(Runtime.getRuntime().availableProcessors()); //获得本机处理器数
        long maxMemory = Runtime.getRuntime().maxMemory();//返回 Java 虚拟机试图使用的最大内存量。
        long totalMemory = Runtime.getRuntime().totalMemory();//返回 Java 虚拟机中的总内存总量。
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + "（字节）、" + (maxMemory / (double) 1024 / 1024) + "MB");
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + "（字节）、" + (totalMemory / (double) 1024 / 1024) + "MB");
    }

    /**
     * java.lang.OutOfMemoryError
     */
    @Test
    public void test04() {
        Byte[] bytes = new Byte[40 * 1024 * 1024];
    }

    @Test
    public void test05(){
        int i=10,k =10;
        i=i++;
        int j=++k;
        System.out.println(i);
        System.out.println(j);
        System.out.println(i++);
    }

    static void method01() {
        method01();
    }

    /**
     * 写出OOM
     * Exception in thread "main" java.lang.OutOfMemoryError: Java heap space
     *
     * @param args
     */
/*    public static void main(String[] args) {
        // method01();
        System.out.println(Runtime.getRuntime().availableProcessors()); //获得本机处理器数
        long maxMemory = Runtime.getRuntime().maxMemory() ;//返回 Java 虚拟机试图使用的最大内存量。
        long totalMemory = Runtime.getRuntime().totalMemory() ;//返回 Java 虚拟机中的总内存总量。
        System.out.println("-Xmx:MAX_MEMORY = " + maxMemory + "（字节）、" + (maxMemory / (double)1024 / 1024) + "MB");
        System.out.println("-Xms:TOTAL_MEMORY = " + totalMemory + "（字节）、" + (totalMemory / (double)1024 / 1024) + "MB");
        // Byte[] bytes = new Byte[40*1024*1024];
        // while (true){
        String str = "www.atguigu.com" ;

        while(true)
        {
            str += str + new Random().nextInt(88888888) + new Random().nextInt(999999999) ;
        }

        // Byte[] bytes = new Byte[Integer.MAX_VALUE];//需要除以2，不然Requested array size exceeds VM limit
        // int[] ints = new int[Integer.MAX_VALUE/2];////需要除以2，不然Requested array size exceeds VM limit
        // }
    }*/
    private byte[] bigSize = new byte[2 * 1024 * 1024];//这个成员属性唯一的作用就是占用一点内存
    Object instance = null;

    /**
     * 验证GC
     * @param args
     */
/*    public static void main(String[] args) {
        JVM_Test01 objectA = new JVM_Test01();
        JVM_Test01 objectB = new JVM_Test01();
        objectA.instance = objectB;
        objectB.instance = objectA;
        objectA = null;
        objectB = null;

        // System.gc();//手动调用GC，但不是立刻执行

    }*/

    /**
     * 验证JMM的可见性
     * volatile
     */
/*    public static void main(String[] args) {
        MyNumber myNumber = new MyNumber();
        new Thread(()->{
            System.out.println("=================== come in AAA");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            myNumber.changeNum();
            System.out.println("=================== The number has been changed!");
        }, "AAA").start();

        while (myNumber.number==10){
            // while里面不要println,因为println是sychronized修饰的，会影响内存可见性
        // System.out.println(" =====================main is while!");

        }
        System.out.println(" =====================main is Over!");
    }*/

    /**
     * 验证类的加载顺序
     * 静态static关键字修饰的是方法、变量或代码块是类信息的一部分，随着类一起加载编译，只载入一次
     * 静态代码块>构造块>构造方法
     */
    public static void main(String[] args) {
        System.out.println("=============================进入main方法：");
        new CodeTemplate();
        System.out.println("====================分割线================");
        new CodeTemplate();
        System.out.println("====================分割线================");
        new CodeTemplate00002();
    }
}
class MyNumber{
    // int number=10;
    volatile int number=10;
    public void changeNum(){
        this.number=1024;
    }
}

class CodeTemplate {
    {
        //构造块
        System.out.println("==========这是CodeTemplate构造块----");
    }

    static {
        //静态代码块d
        System.out.println("==========这是CodeTemplate--静态代码块----");
    }
    public CodeTemplate(){
        System.out.println("==========这是CodeTemplate--构造方法----");
    }

}
class CodeTemplate00002 {
    {
        //构造块
        System.out.println("==========这是CodeTemplate00002构造块----");
    }

    static {
        //静态代码块d
        System.out.println("==========这是CodeTemplate00002--静态代码块----");
    }
    public CodeTemplate00002(){
        System.out.println("==========这是CodeTemplate00002--构造方法----");
    }

}
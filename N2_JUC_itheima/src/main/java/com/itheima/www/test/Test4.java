package com.itheima.www.test;


import com.itheima.www.Constants;
import com.itheima.www.n2.util.FileReader;
import lombok.extern.slf4j.Slf4j;

/**
 * 启动线程应该用start()
 * 在main线程里直接调用t1线程的run方法，是main线程执行该方法，并不是t1线程执行，
 * 这是因为Java线程先是就绪态才能进入运行态，下面的代码是主线程main把run()当成了一个普通的方法调用，
 * 而run真正的意义是当线程start进入就绪态后被调度获得时间片时执行run方法，从而由就绪态进入运行态。
 * <p>
 * thread类中的start方法调用的是native关键字修饰里面的start0()方法，而使⽤native关键字说明这个⽅法是原⽣函数，
 * 也就是这个⽅法是⽤C/C++语⾔实现的，并且被编译成了DLL，由java去调⽤。这些函数的实现体在DLL中，JDK的源代码中并不包含，
 * 你应该是看不到的。对于不同的平台它们也是不同的。这也是java的底层机制，实际上java就是在不同的平台上调⽤不同的native⽅法实现对操作系统的访问的。
 */
@Slf4j(topic = "c.Test4")
public class Test4 {

    public static void main(String[] args) {
        Thread t1 = new Thread("t1") {
            @Override
            public void run() {
                log.debug("running...");
                FileReader.read(Constants.MP4_FULL_PATH);
            }
        };

        t1.run();  //是main线程调用，而不是开启t1线程
        t1.start(); // 开启新的线程，thread类中的start方法调用的是native方法，也就是c++方法，又操作系统来new线程
        log.debug("do other things...");
    }
}

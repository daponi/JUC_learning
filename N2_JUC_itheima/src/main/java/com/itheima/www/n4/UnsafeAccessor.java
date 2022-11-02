package com.itheima.www.n4;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 获得unsafe对象的工具类
 * Unsafe 对象提供了非常底层的，操作内存、线程的方法，Unsafe 对象不能直接调用，只能通过反射获得
 * Unsafe 的含义是直接对底层进行操作可能会造成不安全问题，所以类名叫Unsafe
 * Unsafe底层是由内存偏移量来定位属性，再和属性进行比较和交换CAS
 *
 * // 源码里unsafe是单例还是private，所以只能通过反射获得getDeclaredField
 */
public class UnsafeAccessor {
    // 1.先声明一个静态的Unsafe对象，
    private static final Unsafe unsafe ;
    static {
        // 2.源码里unsafe是单例还是private，所以只能通过反射获得getDeclaredField
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            //3.unsafe成员在原类中是私有的private，所以先设置访问权
            theUnsafe.setAccessible(true)    ;
            // 4.unsafe在原类中是静态的，从属于类不属于对象，所以直接get(null)
            unsafe  = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            // e.printStackTrace();
            // 静态代码块没办法把异常再往外抛，因为它是检查异常，可以转换为运行时异常抛出去或直接抛错误Error
            throw new Error(e);
        }

    }
    public static Unsafe getUnsafe(){
        return unsafe;
    }
}

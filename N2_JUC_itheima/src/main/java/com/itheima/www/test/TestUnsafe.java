package com.itheima.www.test;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * Unsafe 对象提供了非常底层的，操作内存、线程的方法，Unsafe 对象不能直接调用，只能通过反射获得
 * Unsafe 的含义是直接对底层进行操作可能会造成不安全问题，所以类名叫Unsafe
 * Unsafe底层是由内存偏移量来定位属性，再和属性进行比较和交换CAS
 *
 * Unsafe CAS 操作：
 *      1.获取域的偏移地址
 *      2.执行CAS操作
 *  Unsafe CAS 参数：是根据对线的偏移量来定位其成员变量
 */
public class TestUnsafe {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        // 源码里unsafe是单例还是private，所以只能通过反射获得getDeclaredField
        Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
        //unsafe是私有的private，所以先设置访问权
        theUnsafe.setAccessible(true);
        // unsafe是静态的，从属于类不属于对象，所以直接get(null)
        Unsafe unsafe = (Unsafe) theUnsafe.get(null);

        System.out.println(unsafe);

        // Unsafe底层是由内存偏移量来定位属性，再和属性进行比较和交换CAS
        // 1.获取域的偏移地址
        long idOfferset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("id"));
        long nameOfferset = unsafe.objectFieldOffset(Teacher.class.getDeclaredField("name"));
        Teacher teacher = new Teacher();
        //2.执行CAS操作  参数：操作对象，偏移量，原值，新值
        System.out.println(unsafe.compareAndSwapInt(teacher, idOfferset, 0, 3));
        System.out.println(unsafe.compareAndSwapObject(teacher, nameOfferset, null, "张三"));
        System.out.println(teacher);


    }
}
class Teacher{
    // 默认什么都不加，就是default(不用把default写出来)。 意思就是只能由跟这个类在同一个包中的类来访问，比private限制更少，但比protected限制更多
    // Java访问修饰符包括private，default，protected和public。含义分别表示私有的，默认的，受保护的和公有的访问修饰符
    volatile int id;
    volatile String name;

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

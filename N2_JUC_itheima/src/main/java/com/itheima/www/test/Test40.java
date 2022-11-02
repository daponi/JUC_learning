package com.itheima.www.test;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 字段更新器
 * AtomicReferenceFieldUpdater // 域 字段
 * AtomicIntegerFieldUpdater
 * AtomicLongFieldUpdater
 *
 * 利用字段更新器，可以针对对象的某个域（Field）进行原子操作，只能配合 volatile 修饰的字段使用，否则会出现异常
 */
public class Test40 {
    public static void main(String[] args) {
        Student strudent = new Student();
        AtomicReferenceFieldUpdater file= AtomicReferenceFieldUpdater.newUpdater(Student.class,String.class,"name");
        System.out.println(file.compareAndSet(strudent, null, "张三"));
        System.out.println(strudent);

    }
}
class Student {
    public volatile String name;

    @Override
    public String toString() {
        return "Strudent{" +
                "name='" + name + '\'' +
                '}';
    }
}

package com.atguigu.www.lock;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * list集合线程不安全
 *多个线程同时对ArrayList集合进行add()修改就可能出现并发修改异常。
 * 3种解决方案：
 * 1. 解决方案-Vector
 * 2. 解决方案-Collections
 * 3. 解决方案-CopyOnWriteArrayList
 * 同理HashSet和HashMap也存在同样的问题，可以使用CopyOnWriteArraySet、ConcurrentHashMap解决
 */
public class ThreadDemo4 {
    public static void main(String[] args) {
        //第一种：创建ArrayList集合进行多线程add()操作会出现并发修改异常
/*       List<String> list = new ArrayList<>();
        for (int i = 0; i <30; i++) {
            new Thread(()->{
                //向集合添加内容
                list.add(UUID.randomUUID().toString().substring(0,8));
                //从集合获取内容
                System.out.println(list);
            },String.valueOf(i)).start();
        }*/

/*================================分割线===============================*/
        // 第一种解决方案：Vector解决
/*       List<String> list = new Vector<>();
        for (int i = 0; i <30; i++) {
            new Thread(()->{
                //向集合添加内容
                list.add(UUID.randomUUID().toString().substring(0,8));
                //从集合获取内容
                System.out.println(list);
            },String.valueOf(i)).start();
        }*/

/*================================分割线===============================*/
        //第二种解决方案:Collections解决
/*       List<String> list = Collections.synchronizedList(new ArrayList<>());
       for (int i = 0; i <30; i++) {
           new Thread(()->{
               //向集合添加内容
               list.add(UUID.randomUUID().toString().substring(0,8));
               //从集合获取内容
               System.out.println(list);
           },String.valueOf(i)).start();
       }*/

/*================================分割线===============================*/
        //第三解决方案 CopyOnWriteArrayList解决
/*       List<String> list = new CopyOnWriteArrayList<>();
        //多个线程同时对集合进行修改
       for (int i = 0; i <30; i++) {
           new Thread(()->{
               //向集合添加内容
               list.add(UUID.randomUUID().toString().substring(0,8));
               //从集合获取内容
               System.out.println(list);
           },String.valueOf(i)).start();
       }*/

        /*================================分割线===============================*/

        //演示Hashset
       // Set<String> set = new HashSet<>();

       // Set<String> set = new CopyOnWriteArraySet<>();
/*       for (int i = 0; i <30; i++) {
           new Thread(()->{
               //向集合添加内容
               set.add(UUID.randomUUID().toString().substring(0,8));
               //从集合获取内容
               System.out.println(set);
           },String.valueOf(i)).start();
       }*/

        /*================================分割线===============================*/

        //演示HashMap
//        Map<String,String> map = new HashMap<>();

        Map<String,String> map = new ConcurrentHashMap<>();
        for (int i = 0; i <30; i++) {
            String key = String.valueOf(i);
            new Thread(()->{
                //向集合添加内容
                map.put(key,UUID.randomUUID().toString().substring(0,8));
                //从集合获取内容
                System.out.println(map);
            },String.valueOf(i)).start();
        }
    }
}

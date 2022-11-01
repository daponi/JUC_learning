package com.itheima.www.test;

/**
 * i=i++ 问题底层原理解析
 * javac -c .java  编译java文件成.class
 * javap -c .class 反编译字节码文件.class
 * 打开字节码文件：
 *  Code:
 *        0: iconst_0
 *        1: istore_1
 *        2: iload_1
 *        3: iinc          1, 1
 *        6: istore_1
 *        7: getstatic     #2                  // Field java/lang/System.out:Ljava/io/PrintStream;
 *       10: iload_1
 *       11: invokevirtual #3                  // Method java/io/PrintStream.println:(I)V
 *       14: return
 *
 * 0: bipush  0  //准备一个常量0压入栈
 * 2：istore_1   //栈中弹出一个数（0），赋值给局部变量i（_1表示赋值给第一个局部变量，即i）
 * 3: iload_1    //将局部变量i(_1表示第一个局部变量，即i)的值入栈，此时栈顶的值为0
 * 4: iinc 1, 1  //指令iinc对给定的局部变量做自增操作。1,1表示对第一个局部变量i进行累加1操作，意味着i变为了1
 * 7: istore_1   //栈顶弹出一个数：也就是0，赋值给第一个局部变量i。意味着i的值又变回0了。（_1表示赋值给第一个局部变量，即i）
 *
 */
public class ATest {
    public static void main(String[] args) {
        int i=0;
        i=i++;
         // i++; //输出为1
        System.out.println(i); // 输出为0
    }
}

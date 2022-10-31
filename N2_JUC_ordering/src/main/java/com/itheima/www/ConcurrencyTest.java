package com.itheima.www;

import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.I_Result;

/**
 * I_Result 是一个对象，有一个属性 r1 用来保存结果，问，可能的结果有几种？
 * 有同学这么分析
 * 情况1：线程1 先执行，这时 ready = false，所以进入 else 分支结果为 1
 * 情况2：线程2 先执行 num = 2，但没来得及执行 ready = true，线程1 执行，还是进入 else 分支，结果为1
 * 情况3：线程2 执行到 ready = true，线程1 执行，这回进入 if 分支，结果为 4（因为 num 已经执行过了）
 * 但我告诉你，结果还有可能是 0
 *
 * 指令重排，是 JIT 编译器在运行时的一些优化，这个现象需要通过大量测试才能复现，如借助 java 并发压测工具 jcstress
 */
@JCStressTest
@Outcome(id = {"1", "4"}, expect = Expect.ACCEPTABLE, desc = "ok")
@Outcome(id = "0", expect = Expect.ACCEPTABLE_INTERESTING, desc = "!!!!")
@State
public class ConcurrencyTest {
    int num = 0;
    boolean ready = false;
    // volatile boolean ready = false; //拥有写屏障
    @Actor
    public void actor1(I_Result r) {
        if(ready) {
            r.r1 = num + num;
        } else {
            r.r1 = 1;
        }
    }
    @Actor
    public void actor2(I_Result r) {
        num = 2;
        ready = true;
    }
}
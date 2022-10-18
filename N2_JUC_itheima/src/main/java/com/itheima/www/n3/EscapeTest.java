package com.itheima.www.n3;

public class EscapeTest {
    public static SomeClass someClass;

    //全局变量赋值逃逸
    public void globalVariablePointerEscape() {
        someClass = new SomeClass();
    }

    //方法返回值逃逸
    //void someMethod(){
    //SomeCLass someCLass = methodPointerEscape () ;
    //    }

    public SomeClass methodPointerEscape() {
        return new SomeClass();
    }

    //实例引用传递逃逸
    public void instancePassPointerEscape() {
        this.methodPointerEscape().printClassName(this);
    }
}

class SomeClass {
    public void printClassName(EscapeTest escapeTest) {
        System.out.println(escapeTest.getClass().getName());
    }
}
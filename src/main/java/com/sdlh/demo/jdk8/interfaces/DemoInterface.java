package com.sdlh.demo.jdk8.interfaces;

public interface DemoInterface {
    default void doSomething() {
        System.out.println("默认方法-->test1");
    }

    static void test() {
        System.out.println("这是DemoInterface的静态方法");
    }
}

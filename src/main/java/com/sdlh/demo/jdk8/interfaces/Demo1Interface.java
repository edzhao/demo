package com.sdlh.demo.jdk8.interfaces;

public interface Demo1Interface {
    default void doSomething() {
        System.out.println("默认方法-->test2");
    }

    static void test() {
        System.out.println("这是Demo1Interface的静态方法");
    }
}

package com.sdlh.demo.jdk8.interfaces;

public class Test implements DemoInterface, Demo1Interface {
    public static void main(String[] args) {
        Test test = new Test();
        test.doSomething();
        DemoInterface.test();
        Demo1Interface.test();
    }

    @Override
    public void doSomething() {
        System.out.println("默认方法-->test3");
    }
}

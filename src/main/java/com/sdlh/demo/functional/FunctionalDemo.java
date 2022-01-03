package com.sdlh.demo.functional;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class FunctionalDemo {
    public static void main(String[] args) {
        testFunctional();
//        testLambdaVariableScope();
    }

    /**
     * 通过闭包将局部变量变成全局变量，延长了变量的作用域，有造成内存泄漏的风险
     *
     * @return
     */
    private static IAdder newAdder() {
        AtomicInteger a = new AtomicInteger(0);
        return () -> a.incrementAndGet();
    }

    private static void testLambdaVariableScope() {
        IAdder adder = newAdder();
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                System.out.println("a=" + adder.add());
            }
        }
    }

    private static void testFunctional() {
        Consumer f = System.out::println;
        Consumer f2 = n -> System.out.println(n + "-F2");
        //执行完F后再执行F2的Accept方法
        f.andThen(f2).accept("test");
        //连续执行F的Accept方法
        f.andThen(f).andThen(f).andThen(f).accept("test1");
    }
}

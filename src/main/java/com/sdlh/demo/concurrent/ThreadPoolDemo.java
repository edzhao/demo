/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class ThreadPoolDemo {
    public static void main(String[] args) {
        log.info("线程池开始执行");
        long start = System.currentTimeMillis();

        LongAdder adder = new LongAdder();

        Thread t1 = new Thread(() -> increment(adder));
        t1.start();

        Thread t2 = new Thread(() -> increment(adder));
        t2.start();

        Thread t3 = new Thread(() -> increment(adder));
        t3.start();

        try {
            Thread.sleep(15);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.stop();
        t2.stop();
        t3.stop();
        log.info("线程池关闭,程序退出,adder=" + adder.sum() + ",花费时间" + (System.currentTimeMillis() - start) + "ms");
    }

    private static void increment(LongAdder adder) {
        for (int i = 0; i <= 999999999;i++) {
            adder.increment();
        }
    }
}

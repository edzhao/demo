package com.sdlh.demo.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

@Slf4j
public class LockSupportDemo {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("进入线程，并且park阻塞当前线程");
            LockSupport.park();
            log.info("线程退出");
        });
        t.setName("worker");
        t.start();
        log.info("worker线程已启动");
        LockSupport.unpark(t);
        log.info("main线程退出");
    }
}

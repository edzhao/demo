/*
 * Copyright 2019 成都深地领航能源科技有限公司. All rights reserved.
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package com.sdlh.demo.concurrent;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author 成都深地领航能源科技有限公司
 */
@Slf4j
public class CompletableFutureDemo {
    public static void main(String[] args) {
        Object full = new Object();
        List<CompletableFuture> futureList = new ArrayList<>();
        futureList.add(CompletableFuture.runAsync(() -> {
            run(1000);
            synchronized (full) {
                try {
                    log.info("线程1阻塞");
                    full.wait();
                    log.info("线程1被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info(Thread.currentThread().getName() + "线程结束");
        }));
        futureList.add(CompletableFuture.runAsync(() -> {
            run(2000);
            log.info(Thread.currentThread().getName() + "线程结束");
        }));
        futureList.add(CompletableFuture.runAsync(() -> {
            run(3000);
            synchronized (full) {
                full.notify();
            }
            log.info(Thread.currentThread().getName() + "线程结束");
        }));
        futureList.add(CompletableFuture.runAsync(() -> {
            run(500);
            synchronized (full) {
                try {
                    log.info("线程4阻塞");
                    full.wait();
                    log.info("线程4被唤醒");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            log.info(Thread.currentThread().getName() + "线程结束");
        }));
        CompletableFuture.allOf(futureList.toArray(new CompletableFuture[futureList.size()])).join();
        log.info("主线程退出");
    }

    private static void run(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }
}

package com.sdlh.demo.concurrent;

public class ThreadDemo {
    public static void main(String[] args) {
        Thread t1 = new Thread(() -> {
            try {
                Thread.sleep(2000);
                System.out.println("t1线程退出");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.setName("thread-1");
//        t1.setDaemon(true);
        t1.start();
        System.out.println("主线程已退出");
    }
}

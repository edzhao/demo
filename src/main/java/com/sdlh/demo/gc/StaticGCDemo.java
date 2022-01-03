package com.sdlh.demo.gc;

import org.openjdk.jol.info.ClassLayout;

import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class StaticGCDemo {
    public static void main(String[] args) throws InterruptedException {
        String time = "2021/06/30 11:19:55.041";
        double data = 245.18664550781247;
        int index = 16999;
        NewMessage message = new NewMessage(time, data, index);
        System.out.println(ClassLayout.parseInstance(message).toPrintable() + "\n\n");
        String str = "1625023195041" + "," + data + "," + 30000;
        System.out.println(ClassLayout.parseInstance(str).toPrintable() + "\n\n");
    }

    public static void testStaticGC() {
//        BlockingQueue<Message> queue = MessageQueue.queue;
//        for (int i=0; i < 50000; i++) {
//            queue.offer(new Message(i, "这是第" + i + "条消息", new Date()));
//        }
//
//        System.out.println("队列中共有" + queue.size() + "个元素");
//        printMemory();
//        System.out.println("-------------------------------before clear----------------------------------");
////        queue.clear();
////        for (int i=0; i < 50000; i++) {
////            queue.poll();
////        }
//        ArrayList<Message> list = new ArrayList<>();
//        queue.drainTo(list, 50000);
//        list.clear();
////        System.gc();
//        TimeUnit.MILLISECONDS.sleep(600);
//        System.out.println("-------------------------------after clear and gc----------------------------------");
//        System.out.println("Queue中共有" + queue.size() + "个元素");
//        System.out.println("List中共有" + list.size() + "个元素");
//        printMemory();
    }

    public static void printMemory() {
        Runtime runtime = Runtime.getRuntime();
        long maxMemory = runtime.maxMemory() / 1024 / 1024;
        long freeMemory = runtime.freeMemory() / 1024 / 1024;
        long totalMemory = runtime.totalMemory() / 1024 / 1024;
        System.out.println("JVM最大可分配内存为:" + maxMemory + "MB");
        System.out.println("JVM已分配内存为:" + totalMemory + "MB");
        System.out.println("JVM空闲内存为:" + freeMemory + "MB");
        System.out.println("已使用的JVM内存为:" + (totalMemory - freeMemory) + "MB");
        System.out.println("可用JVM内存为:" + (maxMemory - totalMemory + freeMemory) + "MB");
    }
}

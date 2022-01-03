package com.sdlh.demo.concurrent;

public class InterruptDemo implements Runnable {
    @Override
    public void run() {
        unHandleInterrupt();
    }

    /**
     * 未处理中断
     */
    public void unHandleInterrupt() {
        int num = 0;
        //打印最大整数一半的范围内10000的倍数
        while (num <= Integer.MAX_VALUE / 2 && !Thread.currentThread().isInterrupted()) {
            if (num % 70 == 0) {
                System.out.println(num + "是70的倍数");
            }
            ++num;
        }
        System.out.println("线程退出");
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new InterruptDemo());
        //启动线程
        thread.start();
        //增加子线程处于运行状态的可能性
//        Thread.sleep(60);
        //尝试中断子线程
        thread.interrupt();
    }
}

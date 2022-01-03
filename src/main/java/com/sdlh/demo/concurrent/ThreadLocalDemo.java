package com.sdlh.demo.concurrent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class ThreadLocalDemo {
    public static final ThreadLocal<User> threadLocal = ThreadLocal.withInitial(() -> User.builder()
                                                                                          .name("Fenix.T")
                                                                                          .age(39)
                                                                                          .sex("M")
                                                                                          .build());
    public static final InheritableThreadLocal<User> inheritableThreadLocal = new InheritableThreadLocal<>();

    public static void main(String[] args) {
        User hutu = new User("Hutu", 8, "F");
        threadLocal.set(hutu);
        inheritableThreadLocal.set(hutu);

        //        Thread t1 = new Thread(() -> run1());
//        t1.setName("thread-1");
//        t1.start();
//
//        Thread t2 = new Thread(() -> run2());
//        t2.setName("thread-2");
//        t2.start();

        Executor pool = new ThreadPoolExecutor(4,
                                                8,
                                                10,
                                                TimeUnit.SECONDS,
                                                new LinkedBlockingQueue<>(),
                                                Executors.defaultThreadFactory(),
                                                new RejectedExecutionHandler() {
                                                @Override
                                                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                                                }
        });

        CompletableFuture.runAsync(() -> run1(), pool);
        CompletableFuture.runAsync(() -> run2(), pool);

        log.info(Thread.currentThread().getName() + ":" + threadLocal.get());
        try {
            ((ThreadPoolExecutor) pool).shutdown();
            ((ThreadPoolExecutor) pool).awaitTermination(500, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
            ((ThreadPoolExecutor) pool).shutdownNow();
        }
    }

    private static void run1() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
            User user = threadLocal.get();
            user.setAge(27);
            threadLocal.set(user);
            TimeUnit.MILLISECONDS.sleep(100);
            user = threadLocal.get();
            user.setSex("F");
            threadLocal.set(user);
            log.info(Thread.currentThread().getName() + ":" + threadLocal.get());
            log.info(Thread.currentThread().getName() + ":" + inheritableThreadLocal.get());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    private static void run2() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
            User user = threadLocal.get();
            user.setName("zak");
            TimeUnit.MILLISECONDS.sleep(100);
            user = threadLocal.get();
            user.setAge(13);
            threadLocal.set(user);
            log.info(Thread.currentThread().getName() + ":" + threadLocal.get());
            log.info(Thread.currentThread().getName() + ":" + inheritableThreadLocal.get());
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

}

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
class User {
    private String name;
    private int age;
    private String sex;
}

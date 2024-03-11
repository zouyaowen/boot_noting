package com.trip.noting;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchTest {

    // CountDownLatch 一个线程等待多线程执行
    // Semaphore 控制同一时间并发线程的数量
    // CyclicBarrier 功能和CountDownLatch类似，但是使用更简单

    // CountDownLatch 和 CyclicBarrier区别

    // CyclicBarrier的await会将线程进行阻塞并且将count值减1，CountDownLatch的await只会将线程阻塞，计数器减1需要调用countdown来执行
    // CyclicBarrier可以实现循环拦截，调用reset方法即可重置栅栏，而CountDownLatch只能拦截一次
    // CyclicBarrier在等待多个线程到达某一条件时，他们是线程间相互监督的。CountDownLatch并不是线程间相互监督，而是有个类似于裁判的角色进行管理的



    public static void main(String[] args) {
        // 创建CountDownLatch并设置计数值，该count值可以根据线程数的需要设置
        // 线程数
        int n = 10;
        CountDownLatch countDownLatch = new CountDownLatch(n);

        // 创建线程池
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

        for (int i = 0; i < n; i++) {
            cachedThreadPool.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " do something!");
                } catch (Exception e) {
                    System.out.println("Exception: do something exception");
                } finally {
                    // 该线程执行完毕-1
                    countDownLatch.countDown();
                }
            });
        }

        System.out.println("main thread do something-1");
        try {
            // 单位：min
            int countDownLatchTimeout = 5;
            boolean await = countDownLatch.await(countDownLatchTimeout, TimeUnit.MINUTES);
            if (await) {
                System.out.println("multi thread finished");
            }
        } catch (InterruptedException e) {
            System.out.println("Exception: await interrupted exception");
        } finally {
            System.out.println("countDownLatch: " + countDownLatch.toString());
        }
        System.out.println("main thread do something-2");
        // 若需要停止线程池可关闭;
//        cachedThreadPool.shutdown();
    }
}

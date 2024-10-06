package com.trip.noting;

import lombok.Getter;
import org.junit.Test;
import org.springframework.util.StopWatch;

import java.util.Date;
import java.util.concurrent.*;

public class TaskTimeTest {
    public static void main(String[] args) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 固定数量线程池
        // for 50*100
        // 5个线程   运行耗时：26603ms
        // 10个线程  运行耗时：14638ms
        // 15个线程  运行耗时：10788ms
        // 20个线程  运行耗时：8796ms
        // 11个线程  运行耗时：13648ms   CPU 是 12核
        // for 100*100
        // 11个线程  运行耗时：49726ms
        // ExecutorService executorService = Executors.newFixedThreadPool(11);
        // 直接扩容的线程池    运行耗时：5126ms
//        ExecutorService executorService = Executors.newCachedThreadPool();
        // for 50*100
        // 11个线程  运行耗时：13955ms
        // for 100*100
        // 运行耗时：50043ms
        //    SAME_CORE - 线程会运行在同一个CPU core中。
        //    SAME_SOCKET - 线程会运行在同一个CPU socket中，但是不在同一个core上。
        //    DIFFERENT_SOCKET - 线程会运行在不同的socket中。
        //    DIFFERENT_CORE - 线程会运行在不同的core上。
        //    ANY - 只要是可用的CPU资源都可以。
//        ExecutorService executorService = Executors.newFixedThreadPool(11, new AffinityThreadFactory("bg", DIFFERENT_SOCKET));
        // for 100*100
        // 运行耗时：49737ms 配置 11 30
        // 运行耗时：49737ms 配置 11 300
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(11, 100, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(1000));
        // 执行任务
        for (int i = 0; i < 100; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    Thread.sleep(finalI * 100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        executorService.shutdown();
        while (true) {
            if (executorService.isTerminated()) {
                System.out.println("结束了！");
                break;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        // 任务结束
        stopWatch.stop();
        long lastTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
        System.out.println("运行耗时：" + lastTaskTimeMillis + "ms");
    }


    public static void compute() {

    }

    @Getter
    public enum StatusEnum {
        LOCK(1), CONFIRM(2);
        private final int status;

        StatusEnum(int status) {
            this.status = status;
        }
    }

    @Test
    public void testTime() {
        Date date = new Date(new Date().getTime() - 30 * 60 * 1000);
        System.out.println(date);
    }

    private StatusEnum getStatus(int status) {
        if (status == 1) {
            return StatusEnum.LOCK;
        }
        return null;
    }
}

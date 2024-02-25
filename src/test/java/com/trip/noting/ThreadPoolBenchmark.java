package com.trip.noting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class ThreadPoolBenchmark {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = 10; // 线程数量
        int numTasks = 1000; // 任务数量

        // 创建线程池
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(numThreads);

        // 创建任务列表
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            Callable<Integer> task = () -> {
                // 执行任务逻辑，这里只是简单的累加操作
                int result = 0;
                for (int j = 0; j < 1000; j++) {
                    result += j;
                }
                return result;
            };
            tasks.add(task);
        }

        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 提交任务并获取CompletableFuture对象
        List<CompletableFuture<Integer>> futures = new ArrayList<>();
        for (Callable<Integer> task : tasks) {
            CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
                try {
                    return task.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }, executor);
            futures.add(future);
        }

        // 等待所有任务完成
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        allFutures.get(); // 阻塞等待所有任务完成

        // 记录结束时间
        long endTime = System.currentTimeMillis();

        // 输出总耗时
        long totalTime = endTime - startTime;
        System.out.println("Total time: " + totalTime + " ms");

        // 关闭线程池
        executor.shutdown();
    }
}
package com.trip.noting;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class IOTaskSimulation {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int numThreads = 10; // 线程数量
        int numTasks = 100; // 任务数量

        // 创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);

        // 创建任务列表
        List<Callable<Integer>> tasks = new ArrayList<>();
        for (int i = 0; i < numTasks; i++) {
            Callable<Integer> task = () -> {
                // 模拟I/O操作，睡眠500毫秒
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 返回任务完成状态
                return 1;
            };
            tasks.add(task);
        }

        // 提交任务并获取Future对象
        List<Future<Integer>> futures = executorService.invokeAll(tasks);

        // 等待所有任务完成
        for (Future<Integer> future : futures) {
            future.get(); // 阻塞等待任务完成
        }

        // 关闭线程池
        executorService.shutdown();
    }
}
package com.trip.noting;

import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

// https://blog.csdn.net/qq_41378597/article/details/129655343
// https://zhuanlan.zhihu.com/p/646768048
@Log4j2
public class TestThreadPool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 异步任务理解
        running8();
    }

    public static void running11() {

    }

    public static void running10() {

    }

    public static void running9() {

    }

    public static void running8() {
        List<String> dataList = Arrays.asList("data1", "data2", "data3");
        List<CompletableFuture<String>> futures = dataList.stream()
                .map(data -> CompletableFuture.supplyAsync(() -> {
                    // 模拟一个耗时操作
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return "Processed " + data;
                }))
                .collect(Collectors.toList());

        List<String> processedDataList = futures.stream()
                // 等待所有异步操作结束
                .map(CompletableFuture::join)
                .collect(Collectors.toList());

        // 输出 ["Processed data1", "Processed data2", "Processed data3"]
        System.out.println(processedDataList);
    }

    public static void running7() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from future1";
        });
        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 模拟一个短时间运行的任务
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from future2";
        });
        CompletableFuture<String> result = future1.applyToEither(future2, str -> "The fastest result is: " + str);
        System.out.println(result.join());  // 输出 "The fastest result is: Result from future2"
    }

    public static void running6() {
        CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from future1";
        });

        CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Result from future2";
        });

        CompletableFuture<String> result = future1.thenCombine(future2, (r1, r2) -> r1 + ", " + r2);
        System.out.println(result.join());  // 输出 "Result from future1, Result from future2"
    }

    public static void running5() {
        // 线程池执行异步任务
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,4,10,TimeUnit.MINUTES,new ArrayBlockingQueue<>(30));
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                String name = Thread.currentThread().getName();
                System.out.println("currentThread getName:"+name);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task result";
        },threadPoolExecutor);
        // 做一些其他的事情
        System.out.println("Do other things...");
        // 获取异步任务的结果
        String result = future.join();
        System.out.println("Result: " + result);
    }

    public static void running4() {
        // 创建一个有返回值的异步任务
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task result";
        });
        // 做一些其他的事情
        System.out.println("Do other things...");
        // 获取异步任务的结果
        String result = future.join();
        System.out.println("Result: " + result);
    }

    public static void running3() {
        // 创建一个没有返回值的异步任务
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> {
            // 模拟一个长时间运行的任务
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Task finished.");
        });
        // 做一些其他的事情
        System.out.println("Do other things...");
        // 等待异步任务完成
        future.join();
    }

    public static void running2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            // 在另一个线程中执行任务
            return 100;
        }).thenApplyAsync(i -> {
            // 在另一个线程中处理上一个任务的结果
            return i * 2;
        }).thenApply(i -> {
            // 在主线程中处理结果
            return i + 1;
        });
        // 获取最终结果
        Integer result = future.join();
        System.out.println(result);  // 输出 201
    }

    public static void running1() throws ExecutionException, InterruptedException {
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            // 这里是你的异步任务
            return "Hello, CompletableFuture!";
        });
        String s = completableFuture.get();
        System.out.println(s);
    }

    @Test
    public void hello() {

    }

}

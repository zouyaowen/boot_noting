package com.trip.noting;


import com.alibaba.fastjson2.JSON;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class HelloWorld {

    public static class AsyncRes {
        public int userRes;
        public int orderRes;
        public int recommendationsRes;
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException {
        System.out.println("------");
        // 启动所有异步任务
        CompletableFuture<Integer> userAsync = getUserAsync();
        CompletableFuture<Integer> orderAsync = getOrderAsync();
        CompletableFuture<Integer> recommendationsAsync = getRecommendationsAsync();

        CompletableFuture<AsyncRes> af = CompletableFuture.allOf(userAsync, orderAsync, recommendationsAsync).thenApply(v -> {
            // 注意：这里get()不会阻塞，因为allOf确保已完成
            Integer join1 = userAsync.join();
            Integer join2 = orderAsync.join();
            Integer join3 = recommendationsAsync.join();
            AsyncRes asyncRes = new AsyncRes();
            asyncRes.userRes = join1;
            asyncRes.orderRes = join2;
            asyncRes.recommendationsRes = join3;
            return asyncRes;
        });
        AsyncRes asyncRes = af.get(600, TimeUnit.MILLISECONDS);
        System.out.println(JSON.toJSONString(asyncRes));
    }

    private static CompletableFuture<Integer> getUserAsync() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        return CompletableFuture.supplyAsync(HelloWorld::getUser, threadPoolTaskExecutor).exceptionally(e -> 0);
    }

    private static CompletableFuture<Integer> getOrderAsync() {
        return CompletableFuture.supplyAsync(HelloWorld::getOrder);
    }

    private static CompletableFuture<Integer> getRecommendationsAsync() {
        return CompletableFuture.supplyAsync(HelloWorld::getRecommendations);
    }

    public static int getUser() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            System.out.println("getUser" + e.getMessage());
        }
        return 1;
    }

    public static int getOrder() {
        try {
            TimeUnit.MILLISECONDS.sleep(200);
        } catch (InterruptedException e) {
            System.out.println("getOrder" + e.getMessage());
        }
        return 2;
    }

    public static int getRecommendations() {
        try {
            TimeUnit.MILLISECONDS.sleep(300);
        } catch (InterruptedException e) {
            System.out.println("getRecommendations" + e.getMessage());
        }
        return 3;
    }
}
